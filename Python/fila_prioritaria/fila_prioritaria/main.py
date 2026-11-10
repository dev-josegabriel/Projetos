import os

from modelos import NIVEIS
from persistencia import carregar, salvar
from servico import FilaAtendimento

ARQUIVO = os.path.join(os.path.dirname(os.path.abspath(__file__)), "dados.json")

MENU = """
========== FILA DE ATENDIMENTO COM PRIORIDADE ==========
 1) Registrar paciente
 2) Chamar próximo
 3) Ver próximo da fila
 4) Listar fila (em ordem de atendimento)
 5) Reclassificar prioridade
 6) Cancelar senha
 7) Histórico e estatísticas
 0) Salvar e sair
========================================================"""


def ler_prioridade():
    print("Níveis de prioridade:")
    for n, nome in NIVEIS.items():
        print(f"  {n} - {nome}")
    try:
        return int(input("Nível (1-5): ").strip())
    except ValueError:
        return -1


def registrar(fila):
    nome = input("Nome do paciente: ")
    prioridade = ler_prioridade()
    try:
        p = fila.registrar(nome, prioridade)
        print(f"✔ Senha {p.senha} gerada para {p.nome} ({p.nivel}).")
    except ValueError as erro:
        print(f"✖ {erro}")


def chamar(fila):
    p = fila.chamar_proximo()
    if p is None:
        print("A fila está vazia.")
    else:
        print(f"📣 Chamando {p.senha} - {p.nome} ({p.nivel})")


def ver_proximo(fila):
    p = fila.proximo()
    print("A fila está vazia." if p is None else f"Próximo: {p.senha} - {p.nome} ({p.nivel})")


def listar(fila):
    itens = fila.listar()
    if not itens:
        print("A fila está vazia.")
        return
    print(f"\n{'#':<3} {'Senha':<6} {'Nome':<22} {'Nível':<14} Chegada")
    for i, p in enumerate(itens, 1):
        print(f"{i:<3} {p.senha:<6} {p.nome:<22} {p.nivel:<14} {p.chegada:%H:%M:%S}")
    print(f"\nTotal na fila: {len(itens)}")


def reclassificar(fila):
    senha = input("Senha (ex: P001): ")
    nova = ler_prioridade()
    try:
        p = fila.reclassificar(senha, nova)
    except ValueError as erro:
        print(f"✖ {erro}")
        return
    print(f"✔ {p.senha} agora é '{p.nivel}'." if p else "✖ Senha não encontrada.")


def cancelar(fila):
    p = fila.cancelar(input("Senha (ex: P001): "))
    print(f"✔ Senha {p.senha} ({p.nome}) cancelada." if p else "✖ Senha não encontrada.")


def historico(fila):
    if not fila.atendidos:
        print("Ninguém foi atendido ainda.")
        return
    print("\nAtendidos:")
    for a in fila.atendidos:
        print(f"  {a['senha']} - {a['nome']} ({NIVEIS[a['prioridade']]}) - esperou {a['espera_seg']}s")
    print("\nEspera média por nível:")
    for nivel, (qtd, media) in fila.estatisticas().items():
        print(f"  {NIVEIS[nivel]:<14} {qtd} atendido(s), média {media:.1f}s")


def main():
    fila = carregar(ARQUIVO)
    acoes = {
        "1": registrar, "2": chamar, "3": ver_proximo, "4": listar,
        "5": reclassificar, "6": cancelar, "7": historico,
    }
    while True:
        print(MENU)
        print(f"Na fila agora: {fila.tamanho()}")
        try:
            op = input("Escolha: ").strip()
        except (EOFError, KeyboardInterrupt):
            op = "0"
        if op == "0":
            salvar(fila, ARQUIVO)
            print("Dados salvos. Até mais!")
            break
        acao = acoes.get(op)
        if acao:
            acao(fila)
        else:
            print("Opção inválida.")


if __name__ == "__main__":
    main()
