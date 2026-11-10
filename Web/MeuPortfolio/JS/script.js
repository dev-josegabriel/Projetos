// ---------- theme toggle ----------
const root = document.documentElement;
const themeToggle = document.getElementById('themeToggle');
const toggleLabel = document.getElementById('toggleLabel');

function applyTheme(theme){
  root.setAttribute('data-theme', theme);
  toggleLabel.textContent = theme === 'dark' ? 'DARK' : 'LIGHT';
  themeToggle.setAttribute('aria-pressed', theme === 'light');
}

// starts on dark by default; respects OS preference for light if set
const prefersLight = window.matchMedia && window.matchMedia('(prefers-color-scheme: light)').matches;
applyTheme(prefersLight ? 'light' : 'dark');

themeToggle.addEventListener('click', () => {
  const current = root.getAttribute('data-theme');
  applyTheme(current === 'dark' ? 'light' : 'dark');
});

// ---------- terminal typing sequence ----------
const terminalBody = document.getElementById('terminalBody');

const lines = [
  { prompt: '$ ', text: 'whoami', type: 'command' },
  { text: 'José Gabriel — Desenvolvedor Full Stack', type: 'output' },
  { prompt: '$ ', text: 'cat stack.txt', type: 'command' },
  { text: 'Java · Spring Boot · PostgreSQL · HTML/CSS/JS', type: 'output' },
  { prompt: '$ ', text: 'status --projetos', type: 'command' },
  { text: '4 sistemas em produção ou em desenvolvimento', type: 'output' },
  { text: '// pronto pra próxima build', type: 'comment' },
];

const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

function renderStatic(){
  terminalBody.innerHTML = lines.map(l => {
    if(l.type === 'command') return `<span class="prompt">${l.prompt}</span>${l.text}`;
    if(l.type === 'comment') return `<span class="comment">${l.text}</span>`;
    return l.text;
  }).join('\n');
}

async function typeSequence(){
  terminalBody.innerHTML = '';
  for(const line of lines){
    const lineEl = document.createElement('div');
    terminalBody.appendChild(lineEl);

    if(line.type === 'command'){
      const promptSpan = document.createElement('span');
      promptSpan.className = 'prompt';
      promptSpan.textContent = line.prompt;
      lineEl.appendChild(promptSpan);
      const textNode = document.createTextNode('');
      lineEl.appendChild(textNode);

      for(let i = 0; i < line.text.length; i++){
        textNode.textContent += line.text[i];
        await sleep(28);
      }
      await sleep(220);
    } else if (line.type === 'comment'){
      lineEl.innerHTML = `<span class="comment">${line.text}</span>`;
      await sleep(180);
    } else {
      lineEl.textContent = line.text;
      await sleep(260);
    }
  }
  const cursor = document.createElement('span');
  cursor.className = 'term-cursor';
  terminalBody.appendChild(cursor);
}

function sleep(ms){ return new Promise(res => setTimeout(res, ms)); }

if(reduceMotion){
  renderStatic();
} else {
  typeSequence();
}

// ---------- footer year ----------
document.getElementById('year').textContent = new Date().getFullYear();

// ---------- hero load-in ----------
window.addEventListener('DOMContentLoaded', () => {
  requestAnimationFrame(() => document.body.classList.add('loaded'));
});

// ---------- nav: sliding indicator + active section ----------
const navLinks = Array.from(document.querySelectorAll('#mainNav a'));
const mobileLinks = Array.from(document.querySelectorAll('#mobileNav a'));
const navIndicator = document.getElementById('navIndicator');

function setActiveLink(target){
  navLinks.forEach(a => a.classList.toggle('active', a.dataset.target === target));
  mobileLinks.forEach(a => a.classList.toggle('active', a.dataset.target === target));
  moveIndicator();
}

function moveIndicator(){
  const active = navLinks.find(a => a.classList.contains('active'));
  if(!active || !navIndicator) return;
  navIndicator.style.left = active.offsetLeft + 'px';
  navIndicator.style.width = active.offsetWidth + 'px';
}

const sections = ['sobre', 'projetos', 'contato']
  .map(id => document.getElementById(id))
  .filter(Boolean);

const sectionObserver = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if(entry.isIntersecting){
      setActiveLink(entry.target.id);
    }
  });
}, { rootMargin: '-45% 0px -50% 0px', threshold: 0 });

sections.forEach(sec => sectionObserver.observe(sec));

window.addEventListener('load', moveIndicator);
window.addEventListener('resize', moveIndicator);

// ---------- mobile menu ----------
const menuToggle = document.getElementById('menuToggle');
const mobileNav = document.getElementById('mobileNav');
const navOverlay = document.getElementById('navOverlay');

function toggleMenu(open){
  const isOpen = open ?? !menuToggle.classList.contains('open');
  menuToggle.classList.toggle('open', isOpen);
  mobileNav.classList.toggle('open', isOpen);
  navOverlay.classList.toggle('open', isOpen);
  menuToggle.setAttribute('aria-expanded', isOpen);
  document.body.style.overflow = isOpen ? 'hidden' : '';
}

menuToggle.addEventListener('click', () => toggleMenu());
navOverlay.addEventListener('click', () => toggleMenu(false));
mobileLinks.forEach(a => a.addEventListener('click', () => toggleMenu(false)));

// ---------- scroll reveal ----------
const revealEls = Array.from(document.querySelectorAll('.reveal'));

// stagger project cards
document.querySelectorAll('.projects .project-card').forEach((card, i) => {
  card.style.transitionDelay = `${i * 90}ms`;
});

const revealObserver = new IntersectionObserver((entries, obs) => {
  entries.forEach(entry => {
    if(entry.isIntersecting){
      entry.target.classList.add('in-view');
      obs.unobserve(entry.target);
    }
  });
}, { threshold: 0.15 });

revealEls.forEach(el => revealObserver.observe(el));
