/* Código compartilhado por todas as páginas: menu lateral, chamadas à API e desenhos comuns. */
const API = 'api/fila';
const $ = (id) => document.getElementById(id);
const ROTULO = { EMERGENCIA: 'Emergência', PRIORITARIO: 'Prioritário', NORMAL: 'Normal' };

const ICONES = {
  users: '<path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.9M16 3.1a4 4 0 0 1 0 7.8"/>',
  alert: '<path d="M10.3 3.9 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0z"/><path d="M12 9v4M12 17h.01"/>',
  star: '<path d="M12 2l3.1 6.3 6.9 1-5 4.9 1.2 6.8L12 17.8 5.8 21l1.2-6.8-5-4.9 6.9-1z"/>',
  check: '<circle cx="12" cy="12" r="9"/><path d="M8 12l3 3 5-6"/>',
  plus: '<path d="M12 5v14M5 12h14"/>',
  x: '<path d="M18 6 6 18M6 6l12 12"/>',
  clock: '<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
  megaphone: '<path d="M3 11v2a1 1 0 0 0 1 1h2l5 4V6L6 10H4a1 1 0 0 0-1 1z"/><path d="M15 9a4 4 0 0 1 0 6M18 6a8 8 0 0 1 0 12"/>',
  list: '<path d="M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01"/>',
  layers: '<path d="M12 2 2 7l10 5 10-5z"/><path d="M2 17l10 5 10-5M2 12l10 5 10-5"/>',
  arrow: '<path d="M5 12h14M13 6l6 6-6 6"/>',
  inbox: '<path d="M22 12h-6l-2 3h-4l-2-3H2"/><path d="M5.5 5.1 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.5-6.9A2 2 0 0 0 16.8 4H7.2a2 2 0 0 0-1.7 1.1z"/>',
  heart: '<path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1-1.1a5.5 5.5 0 0 0-7.8 7.8l1 1.1L12 21l7.8-7.5 1-1.1a5.5 5.5 0 0 0 0-7.8z"/>',
  grid: '<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>',
  book: '<path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20V3H6.5A2.5 2.5 0 0 0 4 5.5z"/><path d="M4 19.5A2.5 2.5 0 0 0 6.5 22H20v-5"/>',
  calendar: '<rect x="3" y="4" width="18" height="18" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/>'
};
const ic = (nome) => '<svg class="ic" aria-hidden="true" viewBox="0 0 24 24">' + ICONES[nome] + '</svg>';

const PAGINAS = [
  ['index.html', 'painel', 'Painel', 'grid'],
  ['fila.html', 'fila', 'Fila de espera', 'list'],
  ['historico.html', 'historico', 'Histórico', 'clock'],
  ['regras.html', 'regras', 'Regras', 'book']
];

function esc(texto) {
  const d = document.createElement('div');
  d.textContent = texto;
  return d.innerHTML;
}

function aviso(msg, ok) {
  const t = $('toast');
  t.textContent = msg;
  t.className = 'mostrar' + (ok ? ' ok' : '');
  clearTimeout(aviso.timer);
  aviso.timer = setTimeout(() => { t.className = ''; }, 2800);
}

/* ---- Modo demonstração: usado só quando não existe servidor (ex.: GitHub Pages).
   Repete as mesmas regras do FilaService, guardando os dados no navegador. ---- */
let modoLocal = false;
const NIVEL = { EMERGENCIA: 1, PRIORITARIO: 2, NORMAL: 3 };
const CHAVE = 'fila-atendimento-demo';
let estadoMemoria = null;

function carregarEstado() {
  try { const t = localStorage.getItem(CHAVE); if (t) { return JSON.parse(t); } } catch (e) { /* sem storage */ }
  return estadoMemoria || { fila: [], historico: [], proximoId: 1, atendidos: 0, chamado: null };
}
function salvarEstado(e) {
  estadoMemoria = e;
  try { localStorage.setItem(CHAVE, JSON.stringify(e)); } catch (err) { /* sem storage */ }
}
function agora() { const d = new Date(); return String(d.getHours()).padStart(2, '0') + ':' + String(d.getMinutes()).padStart(2, '0'); }

function respostaLocal(e) {
  const fila = e.fila.slice().sort((a, b) => NIVEL[a.prioridade] - NIVEL[b.prioridade] || a.id - b.id);
  const conta = (p) => fila.filter((x) => x.prioridade === p).length;
  return { fila: fila, historico: e.historico, total: fila.length, emergencias: conta('EMERGENCIA'),
           prioritarios: conta('PRIORITARIO'), normais: conta('NORMAL'), atendidos: e.atendidos, chamado: e.chamado };
}

function apiLocal(url, opcoes) {
  const metodo = ((opcoes && opcoes.method) || 'GET').toUpperCase();
  const e = carregarEstado();
  if (metodo === 'POST' && url.endsWith('/chamar')) {
    if (!e.fila.length) { throw new Error('Não há ninguém na fila.'); }
    const proximo = respostaLocal(e).fila[0];
    e.fila = e.fila.filter((x) => x.id !== proximo.id);
    e.chamado = proximo; e.atendidos++;
    e.historico.unshift(Object.assign({}, proximo, { chamadoEm: agora() }));
  } else if (metodo === 'POST') {
    const corpo = JSON.parse(opcoes.body);
    const nome = (corpo.nome || '').trim();
    if (!nome) { throw new Error('O nome é obrigatório.'); }
    if (!NIVEL[corpo.prioridade]) { throw new Error('Escolha a prioridade.'); }
    if (e.fila.some((x) => x.nome.toLowerCase() === nome.toLowerCase())) { throw new Error('Essa pessoa já está na fila.'); }
    const id = e.proximoId++;
    e.fila.push({ id: id, senha: corpo.prioridade[0] + String(id).padStart(3, '0'), nome: nome, prioridade: corpo.prioridade, chegada: agora() });
  } else if (metodo === 'DELETE') {
    const id = Number(url.split('/').pop());
    if (!e.fila.some((x) => x.id === id)) { throw new Error('Essa pessoa não está mais na fila.'); }
    e.fila = e.fila.filter((x) => x.id !== id);
  }
  salvarEstado(e);
  return respostaLocal(e);
}

async function servidorExiste() {
  try {
    const r = await fetch(API);
    return r.ok && (r.headers.get('content-type') || '').includes('json');
  } catch (e) { return false; }
}

async function chamar(url, opcoes) {
  if (modoLocal) { return apiLocal(url, opcoes); }
  const resp = await fetch(url, Object.assign({ headers: { 'Content-Type': 'application/json' } }, opcoes));
  let dados = {};
  try { dados = await resp.json(); } catch (e) { /* resposta sem corpo */ }
  if (!resp.ok) { throw new Error(dados.erro || 'Algo deu errado.'); }
  return dados;
}

let desenharPagina = () => {};

async function executar(promessa, mensagemOk) {
  try {
    const dados = await promessa;
    desenhar(dados);
    if (mensagemOk) { aviso(mensagemOk, true); }
    return true;
  } catch (e) {
    aviso(e.message);
    return false;
  }
}

function desenhar(dados) {
  const conta = document.querySelector('.menu a[data-pagina=fila] .conta');
  if (conta) { conta.textContent = dados.total; }
  const texto = (id, valor) => { const el = $(id); if (el) { el.textContent = valor; } };
  texto('cTotal', dados.total); texto('cEmerg', dados.emergencias);
  texto('cPrio', dados.prioritarios); texto('cAtend', dados.atendidos);
  if ($('chamadoBox')) {
    $('chamadoBox').innerHTML = dados.chamado
      ? '<div class="senha-grande">' + esc(dados.chamado.senha) + '</div>' +
        '<div class="quem">' + esc(dados.chamado.nome) + '</div>' +
        '<span class="badge ' + dados.chamado.prioridade + '">' + ROTULO[dados.chamado.prioridade] + '</span>'
      : '<div class="vazio-chamado">Ninguém foi chamado ainda.</div>';
  }
  if ($('chamar')) { $('chamar').disabled = dados.total === 0; }
  if ($('pPrimeiro')) {
    const ult = dados.fila[dados.fila.length - 1];
    texto('pPrimeiro', dados.fila.length ? dados.fila[0].nome + ' (' + dados.fila[0].senha + ')' : '—');
    texto('pUltimo', ult ? ult.nome + ' (' + ult.senha + ')' : '—');
    texto('pTam', dados.total + (dados.total === 1 ? ' pessoa' : ' pessoas'));
  }
  desenharPagina(dados);
}

/** Uma linha da fila. Com cancelar = true aparece o botão X. */
function itemFila(p, i, cancelar) {
  const li = document.createElement('li');
  li.className = 'p-' + p.prioridade + (i === 0 ? ' proximo' : '');
  li.innerHTML =
    '<span class="ordem">' + (i + 1) + '</span>' +
    '<span class="senha">' + esc(p.senha) + '</span>' +
    '<span class="nome">' + esc(p.nome) + '</span>' +
    '<span class="badge ' + p.prioridade + '">' + ROTULO[p.prioridade] + '</span>' +
    '<span class="hora">' + ic('clock') + esc(p.chegada) + '</span>' +
    (cancelar ? '<button class="icone" title="Saiu da fila">' + ic('x') + '</button>' : '');
  if (cancelar) {
    li.querySelector('button').addEventListener('click', () =>
      executar(chamar(API + '/' + p.id, { method: 'DELETE' }), 'Pessoa removida da fila.'));
  }
  return li;
}

function listaVazia(ul, texto) {
  ul.innerHTML = '<div class="vazio">' + ic('inbox') + '<span>' + texto + '</span></div>';
}

function minutos(hhmm) { const [h, m] = hhmm.split(':').map(Number); return h * 60 + m; }
function espera(chegada, chamadoEm) {
  let d = minutos(chamadoEm) - minutos(chegada);
  if (d < 0) { d += 24 * 60; }
  return d === 0 ? 'menos de 1 min' : d + ' min';
}

function ligarChamar() {
  if ($('chamar')) {
    $('chamar').addEventListener('click', () => executar(chamar(API + '/chamar', { method: 'POST' }), 'Próximo chamado.'));
  }
}

/** Monta o menu lateral e o topo em volta do <main> da página. */
function montarShell() {
  const corpo = document.body;
  const pagina = corpo.dataset.pagina;
  const main = document.querySelector('main');
  const hoje = new Date().toLocaleDateString('pt-BR', { weekday: 'long', day: '2-digit', month: 'long' });
  const app = document.createElement('div');
  app.className = 'app';
  app.innerHTML =
    '<aside class="lateral">' +
      '<div class="marca"><div class="logo">' + ic('heart') + '</div><div><b>Fila de Atendimento</b><span>Triagem com prioridade</span></div></div>' +
      '<nav class="menu">' + PAGINAS.map((p) =>
        '<a href="' + p[0] + '" data-pagina="' + p[1] + '" class="' + (p[1] === pagina ? 'ativo' : '') + '">' + ic(p[3]) + p[2] +
        (p[1] === 'fila' ? '<span class="conta">0</span>' : '') + '</a>').join('') + '</nav>' +
      '<div class="rodape">Java · Spring Boot<br>HTML · CSS · JavaScript</div>' +
    '</aside>' +
    '<div class="principal"><header class="topo"><div><h1>' + corpo.dataset.titulo + '</h1><p>' + corpo.dataset.sub + '</p></div>' +
    '<div class="data">' + ic('calendar') + hoje + '</div></header></div>';
  main.parentNode.insertBefore(app, main);
  app.querySelector('.principal').appendChild(main);
  corpo.insertAdjacentHTML('beforeend', '<div id="toast"></div>');
}

/** Cada página chama iniciar(função que desenha o que é só dela). */
async function iniciar(desenhaDaPagina) {
  montarShell();
  modoLocal = !(await servidorExiste());
  if (modoLocal) {
    const pilula = document.querySelector('.topo .data');
    pilula.style.background = '#fffbeb'; pilula.style.color = '#b45309';
    pilula.title = 'Sem servidor Java: os dados ficam salvos só neste navegador.';
    pilula.lastChild.textContent = ' Modo demonstração · ' + pilula.lastChild.textContent.trim();
  }
  desenharPagina = desenhaDaPagina || (() => {});
  ligarChamar();
  executar(chamar(API));
  setInterval(() => { chamar(API).then(desenhar).catch(() => {}); }, 5000);
}
