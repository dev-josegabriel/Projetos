const API = 'http://localhost:8080/tasks';
let allTasks = [];
let taskModalInstance = null;
let deleteModalInstance = null;
let pendingDeleteId = null;
let pendingDeleteTitle = null;
let confirmDelete = true;
let showToasts = true;

// ==================== INIT ====================
document.addEventListener('DOMContentLoaded', () => {
    taskModalInstance = new bootstrap.Modal(document.getElementById('taskModal'));
    deleteModalInstance = new bootstrap.Modal(document.getElementById('deleteModal'));
    document.getElementById('confirmDeleteBtn').addEventListener('click', confirmDeleteTask);
    setupSidebar();
    setupBtnNewTask();
    setupSaveBtn();
    setupSearch();
    setupFilters();
    setupConfig();
    loadTasks();
});

// ==================== SIDEBAR NAVIGATION ====================
function setupSidebar() {
    document.querySelectorAll('.nav-item[data-section]').forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            const section = item.dataset.section;
            navigateTo(section);
        });
    });
}

function navigateTo(section) {
    document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
    document.querySelector(`.nav-item[data-section="${section}"]`).classList.add('active');

    document.querySelectorAll('.app-section').forEach(s => s.classList.add('hidden'));
    document.getElementById(`section-${section}`).classList.remove('hidden');

    const titles = {
        dashboard: ['Dashboard', 'Visão geral das tarefas'],
        tarefas: ['Tarefas', 'Lista completa de tarefas'],
        relatorios: ['Relatórios', 'Análise e estatísticas'],
        configuracoes: ['Configurações', 'Preferências do sistema']
    };
    document.getElementById('pageTitle').textContent = titles[section][0];
    document.getElementById('pageSubtitle').textContent = titles[section][1];

    const showSearch = section === 'dashboard' || section === 'tarefas';
    document.getElementById('searchBoxWrap').style.display = showSearch ? 'flex' : 'none';

    if (section === 'relatorios') renderRelatorios(allTasks);
    if (section === 'tarefas') renderTable(allTasks);
}

// ==================== MODAL ====================
function setupBtnNewTask() {
    document.getElementById('btnNewTask').addEventListener('click', () => {
        openCreate();
    });
}

function openCreate() {
    document.getElementById('modalTitle').textContent = 'NOVA TAREFA';
    document.getElementById('taskId').value = '';
    document.getElementById('taskTitle').value = '';
    document.getElementById('taskDescription').value = '';
    document.getElementById('taskStatus').value = 'PENDENTE';
    document.getElementById('taskPriority').value = '';
    document.getElementById('formError').textContent = '';
    taskModalInstance.show();
}

function openEdit(task) {
    document.getElementById('modalTitle').textContent = 'EDITAR TAREFA';
    document.getElementById('taskId').value = task.id;
    document.getElementById('taskTitle').value = task.title;
    document.getElementById('taskDescription').value = task.description || '';
    document.getElementById('taskStatus').value = task.status;
    document.getElementById('taskPriority').value = task.priority;
    document.getElementById('formError').textContent = '';
    taskModalInstance.show();
}

function setupSaveBtn() {
    document.getElementById('saveTaskBtn').addEventListener('click', saveTask);
}

// ==================== CRUD ====================
async function loadTasks() {
    try {
        const res = await fetch(API);
        if (!res.ok) throw new Error();
        allTasks = await res.json();
        renderDashboard(allTasks);
        renderTable(allTasks);
    } catch {
        showToast('Erro ao conectar com a API', 'error');
    }
}

async function saveTask() {
    const id = document.getElementById('taskId').value;
    const title = document.getElementById('taskTitle').value.trim();
    const description = document.getElementById('taskDescription').value.trim();
    const status = document.getElementById('taskStatus').value;
    const priority = parseInt(document.getElementById('taskPriority').value);
    const errorEl = document.getElementById('formError');
    errorEl.textContent = '';

    if (!title) { errorEl.textContent = '> Título é obrigatório'; return; }
    if (!priority || priority < 1 || priority > 5) { errorEl.textContent = '> Prioridade deve ser entre 1 e 5'; return; }

    const body = { title, description, status, priority };
    try {
        const res = await fetch(id ? `${API}/${id}` : API, {
            method: id ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        if (!res.ok) { const msg = await res.text(); errorEl.textContent = '> ' + msg; return; }
        taskModalInstance.hide();
        showToast(id ? 'Tarefa atualizada!' : 'Tarefa criada!', 'success');
        await loadTasks();
    } catch {
        errorEl.textContent = '> Erro ao salvar tarefa';
    }
}

function deleteTask(id, title) {
    if (!confirmDelete) {
        doDelete(id);
        return;
    }
    pendingDeleteId = id;
    pendingDeleteTitle = title || 'esta tarefa';
    document.querySelector('.delete-task-name').textContent = pendingDeleteTitle;
    deleteModalInstance.show();
}

async function confirmDeleteTask() {
    if (!pendingDeleteId) return;
    await doDelete(pendingDeleteId);
    deleteModalInstance.hide();
    pendingDeleteId = null;
}

async function doDelete(id) {
    try {
        const res = await fetch(`${API}/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error();
        showToast('Tarefa deletada!', 'success');
        await loadTasks();
    } catch {
        showToast('Erro ao deletar tarefa', 'error');
    }
}

// ==================== RENDER DASHBOARD ====================
function renderDashboard(tasks) {
    const p = tasks.filter(t => t.status === 'PENDENTE');
    const a = tasks.filter(t => t.status === 'EM_ANDAMENTO');
    const c = tasks.filter(t => t.status === 'CONCLUIDA');

    document.getElementById('totalCount').textContent = tasks.length;
    document.getElementById('pendenteCount').textContent = p.length;
    document.getElementById('andamentoCount').textContent = a.length;
    document.getElementById('concluidaCount').textContent = c.length;
    document.getElementById('pendenteBadge').textContent = p.length;
    document.getElementById('andamentoBadge').textContent = a.length;
    document.getElementById('concluidaBadge').textContent = c.length;

    renderList('listPendente', p);
    renderList('listAndamento', a);
    renderList('listConcluida', c);
}

function renderList(containerId, tasks) {
    const el = document.getElementById(containerId);
    if (tasks.length === 0) {
        el.innerHTML = '<div class="empty-state"><i class="bi bi-inbox"></i>Sem tarefas</div>';
        return;
    }
    el.innerHTML = tasks.map(t => taskCard(t)).join('');
}

function priorityLabel(p) {
    if (p <= 2) return 'BAIXA';
    if (p === 3) return 'MEDIA';
    return 'ALTA';
}

function escHtml(str) {
    if (!str) return '';
    return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

function taskCard(task) {
    const cls = task.status.toLowerCase().replace('_', '_');
    const desc = task.description ? `<p class="task-card-desc">${escHtml(task.description)}</p>` : '';
    const data = escHtml(JSON.stringify(task));
    return `
    <div class="task-card ${cls}" onclick='openEdit(${JSON.stringify(task)})'>
        <p class="task-card-title">${escHtml(task.title)}</p>
        ${desc}
        <div class="task-card-footer">
            <span class="priority-badge priority-${task.priority}">P${task.priority} · ${priorityLabel(task.priority)}</span>
            <div class="task-card-actions">
                <button class="btn-action" title="Editar" onclick="event.stopPropagation(); openEdit(${JSON.stringify(task).replace(/"/g,'&quot;')})"><i class="bi bi-pencil"></i></button>
                <button class="btn-action btn-delete" title="Deletar" onclick="event.stopPropagation(); deleteTask(${task.id}, '${escHtml(task.title)}')"><i class="bi bi-trash3"></i></button>
            </div>
        </div>
    </div>`;
}

// ==================== RENDER TABLE ====================
function setupFilters() {
    document.getElementById('filterStatus').addEventListener('change', applyFilters);
    document.getElementById('filterPriority').addEventListener('change', applyFilters);
}

function applyFilters() {
    const status = document.getElementById('filterStatus').value;
    const priority = document.getElementById('filterPriority').value;
    let filtered = allTasks;
    if (status) filtered = filtered.filter(t => t.status === status);
    if (priority) filtered = filtered.filter(t => t.priority == priority);
    renderTable(filtered);
}

function renderTable(tasks) {
    const status = document.getElementById('filterStatus').value;
    const priority = document.getElementById('filterPriority').value;
    let filtered = tasks;
    if (status) filtered = filtered.filter(t => t.status === status);
    if (priority) filtered = filtered.filter(t => t.priority == priority);

    document.getElementById('tableCount').textContent = `${filtered.length} tarefa${filtered.length !== 1 ? 's' : ''}`;
    const tbody = document.getElementById('taskTableBody');
    if (filtered.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center; color: var(--text-muted); font-family: var(--font-mono); padding: 28px;">Nenhuma tarefa encontrada</td></tr>';
        return;
    }
    const statusLabel = { PENDENTE: 'Pendente', EM_ANDAMENTO: 'Em Andamento', CONCLUIDA: 'Concluída' };
    tbody.innerHTML = filtered.map(t => {
        const cls = t.status.toLowerCase().replace('_', '_');
        return `<tr>
            <td style="font-family:var(--font-mono); color:var(--text-muted); font-size:12px;">#${t.id}</td>
            <td style="font-weight:600;">${escHtml(t.title)}</td>
            <td style="color:var(--text-secondary); max-width:200px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">${escHtml(t.description || '—')}</td>
            <td><span class="status-pill ${cls}">${statusLabel[t.status]}</span></td>
            <td><span class="priority-badge priority-${t.priority}">P${t.priority} · ${priorityLabel(t.priority)}</span></td>
            <td><div class="td-actions">
                <button class="btn-action" onclick='openEdit(${JSON.stringify(t)})'><i class="bi bi-pencil"></i></button>
                <button class="btn-action btn-delete" onclick="deleteTask(${t.id}, '${escHtml(t.title)}')"><i class="bi bi-trash3"></i></button>
            </div></td>
        </tr>`;
    }).join('');
}

// ==================== RENDER RELATORIOS ====================
function renderRelatorios(tasks) {
    const total = tasks.length || 1;
    const p = tasks.filter(t => t.status === 'PENDENTE').length;
    const a = tasks.filter(t => t.status === 'EM_ANDAMENTO').length;
    const c = tasks.filter(t => t.status === 'CONCLUIDA').length;

    const statusData = [
        { label: 'Pendente', val: p, color: 'var(--amber)' },
        { label: 'Em Andamento', val: a, color: 'var(--neon)' },
        { label: 'Concluída', val: c, color: 'var(--red)' },
    ];
    document.getElementById('chartStatus').innerHTML = statusData.map(d => `
        <div class="chart-row">
            <span class="chart-label">${d.label}</span>
            <div class="chart-bar-wrap"><div class="chart-bar" style="width:${Math.round(d.val/total*100)}%; background:${d.color};"></div></div>
            <span class="chart-val">${d.val}</span>
        </div>`).join('');

    const pCounts = [1,2,3,4,5].map(n => ({ label: `P${n}`, val: tasks.filter(t => t.priority === n).length, color: n <= 2 ? 'var(--blue)' : n === 3 ? 'var(--amber)' : 'var(--red)' }));
    document.getElementById('chartPriority').innerHTML = pCounts.map(d => `
        <div class="chart-row">
            <span class="chart-label">${d.label}</span>
            <div class="chart-bar-wrap"><div class="chart-bar" style="width:${Math.round(d.val/total*100)}%; background:${d.color};"></div></div>
            <span class="chart-val">${d.val}</span>
        </div>`).join('');

    const pct = n => total > 1 ? Math.round(n / (total) * 100) + '%' : '0%';
    document.getElementById('resumoStats').innerHTML = `
        <div class="resumo-item"><span class="resumo-val">${tasks.length}</span><span class="resumo-label">Total</span><span class="resumo-pct">100%</span></div>
        <div class="resumo-item"><span class="resumo-val" style="color:var(--amber)">${p}</span><span class="resumo-label">Pendente</span><span class="resumo-pct">${pct(p)}</span></div>
        <div class="resumo-item"><span class="resumo-val">${a}</span><span class="resumo-label">Em Andamento</span><span class="resumo-pct">${pct(a)}</span></div>
        <div class="resumo-item"><span class="resumo-val" style="color:var(--red)">${c}</span><span class="resumo-label">Concluída</span><span class="resumo-pct">${pct(c)}</span></div>`;
}

// ==================== SEARCH ====================
function setupSearch() {
    document.getElementById('searchInput').addEventListener('input', (e) => {
        const q = e.target.value.toLowerCase();
        const filtered = q
            ? allTasks.filter(t => t.title.toLowerCase().includes(q) || (t.description && t.description.toLowerCase().includes(q)))
            : allTasks;
        renderDashboard(filtered);
        renderTable(filtered);
    });
}

// ==================== CONFIGURAÇÕES ====================
function setupConfig() {
    document.querySelectorAll('.color-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.color-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            document.documentElement.style.setProperty('--neon', btn.dataset.color);
            const hex = btn.dataset.color;
            const r = parseInt(hex.slice(1,3),16), g = parseInt(hex.slice(3,5),16), b = parseInt(hex.slice(5,7),16);
            document.documentElement.style.setProperty('--neon-glow', `rgba(${r},${g},${b},0.15)`);
            document.documentElement.style.setProperty('--neon-glow-strong', `rgba(${r},${g},${b},0.3)`);
            document.documentElement.style.setProperty('--neon-dim', hex);
            document.documentElement.style.setProperty('--border-hover', `rgba(${r},${g},${b},0.35)`);
        });
    });

    document.getElementById('toggleScanline').addEventListener('change', (e) => {
        document.body.classList.toggle('no-scanline', !e.target.checked);
    });

    document.getElementById('toggleAnimations').addEventListener('change', (e) => {
        document.body.classList.toggle('no-animations', !e.target.checked);
    });

    document.getElementById('toggleConfirmDelete').addEventListener('change', (e) => {
        confirmDelete = e.target.checked;
    });

    document.getElementById('toggleToasts').addEventListener('change', (e) => {
        showToasts = e.target.checked;
    });
}

// ==================== TOAST ====================
function showToast(msg, type = 'success') {
    if (!showToasts) return;
    const container = document.getElementById('toastContainer');
    const icon = type === 'success' ? 'bi-check2-circle' : 'bi-exclamation-circle';
    const el = document.createElement('div');
    el.className = `toast-msg ${type}`;
    el.innerHTML = `<i class="bi ${icon}"></i>${msg}`;
    container.appendChild(el);
    setTimeout(() => el.remove(), 3000);
}