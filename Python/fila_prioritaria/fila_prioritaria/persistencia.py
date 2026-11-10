import json
import os

from servico import FilaAtendimento


def salvar(fila, caminho):
    with open(caminho, "w", encoding="utf-8") as f:
        json.dump(fila.para_dict(), f, ensure_ascii=False, indent=2)


def carregar(caminho):
    if not os.path.exists(caminho):
        return FilaAtendimento()
    try:
        with open(caminho, "r", encoding="utf-8") as f:
            return FilaAtendimento.de_dict(json.load(f))
    except (json.JSONDecodeError, KeyError, ValueError):
        print("Aviso: arquivo de dados corrompido, começando do zero.")
        return FilaAtendimento()
