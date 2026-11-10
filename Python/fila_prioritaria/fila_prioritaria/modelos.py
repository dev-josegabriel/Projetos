from dataclasses import dataclass, field
from datetime import datetime

NIVEIS = {
    1: "Emergência",
    2: "Muito urgente",
    3: "Urgente",
    4: "Pouco urgente",
    5: "Não urgente",
}


@dataclass
class Paciente:
    senha: str
    nome: str
    prioridade: int
    ordem: int  # ordem de chegada, usada para desempate
    chegada: datetime = field(default_factory=datetime.now)

    def __lt__(self, outro):
        # menor prioridade numérica = mais grave = sai primeiro
        return (self.prioridade, self.ordem) < (outro.prioridade, outro.ordem)

    @property
    def nivel(self):
        return NIVEIS[self.prioridade]

    def para_dict(self):
        return {
            "senha": self.senha,
            "nome": self.nome,
            "prioridade": self.prioridade,
            "ordem": self.ordem,
            "chegada": self.chegada.isoformat(),
        }

    @staticmethod
    def de_dict(d):
        return Paciente(
            senha=d["senha"],
            nome=d["nome"],
            prioridade=d["prioridade"],
            ordem=d["ordem"],
            chegada=datetime.fromisoformat(d["chegada"]),
        )
