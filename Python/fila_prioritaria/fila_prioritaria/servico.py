from datetime import datetime

from estruturas.heap import MinHeap
from modelos import NIVEIS, Paciente


class FilaAtendimento:
    def __init__(self):
        self._heap = MinHeap()
        self._contador = 0
        self.atendidos = []  # histórico: lista de dicts

    # ---------- operações principais ----------
    def registrar(self, nome, prioridade):
        nome = (nome or "").strip()
        if not nome:
            raise ValueError("O nome não pode ser vazio.")
        if prioridade not in NIVEIS:
            raise ValueError("Prioridade inválida (use 1 a 5).")
        self._contador += 1
        paciente = Paciente(
            senha=f"P{self._contador:03d}",
            nome=nome,
            prioridade=prioridade,
            ordem=self._contador,
        )
        self._heap.inserir(paciente)
        return paciente

    def proximo(self):
        return self._heap.espiar()

    def chamar_proximo(self):
        paciente = self._heap.remover_topo()
        if paciente is None:
            return None
        espera = (datetime.now() - paciente.chegada).total_seconds()
        self.atendidos.append(
            {
                "senha": paciente.senha,
                "nome": paciente.nome,
                "prioridade": paciente.prioridade,
                "espera_seg": round(espera, 1),
            }
        )
        return paciente

    def cancelar(self, senha):
        senha = senha.strip().upper()
        return self._heap.remover_se(lambda p: p.senha == senha)

    def reclassificar(self, senha, nova_prioridade):
        if nova_prioridade not in NIVEIS:
            raise ValueError("Prioridade inválida (use 1 a 5).")
        senha = senha.strip().upper()
        paciente = self._heap.remover_se(lambda p: p.senha == senha)
        if paciente is None:
            return None
        paciente.prioridade = nova_prioridade  # 'ordem' original é mantida
        self._heap.inserir(paciente)
        return paciente

    def listar(self):
        return self._heap.listar_ordenado()

    def tamanho(self):
        return len(self._heap)

    def estatisticas(self):
        """Tempo médio de espera (seg) por nível, considerando os já atendidos."""
        por_nivel = {}
        for a in self.atendidos:
            por_nivel.setdefault(a["prioridade"], []).append(a["espera_seg"])
        return {
            nivel: (len(valores), sum(valores) / len(valores))
            for nivel, valores in sorted(por_nivel.items())
        }

    # ---------- (de)serialização ----------
    def para_dict(self):
        return {
            "contador": self._contador,
            "fila": [p.para_dict() for p in self._heap.itens()],
            "atendidos": self.atendidos,
        }

    @staticmethod
    def de_dict(d):
        fila = FilaAtendimento()
        fila._contador = d.get("contador", 0)
        fila.atendidos = d.get("atendidos", [])
        for item in d.get("fila", []):
            fila._heap.inserir(Paciente.de_dict(item))
        return fila
