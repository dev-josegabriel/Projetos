import os
import random
import sys
import unittest

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from estruturas.heap import MinHeap
from servico import FilaAtendimento


class TestMinHeap(unittest.TestCase):
    def test_saida_ordenada(self):
        valores = [random.randint(0, 1000) for _ in range(200)]
        h = MinHeap()
        for v in valores:
            h.inserir(v)
        saida = []
        while not h.esta_vazia():
            saida.append(h.remover_topo())
        self.assertEqual(saida, sorted(valores))

    def test_vazia(self):
        h = MinHeap()
        self.assertIsNone(h.espiar())
        self.assertIsNone(h.remover_topo())

    def test_remover_se_mantem_ordem(self):
        h = MinHeap()
        for v in [5, 3, 8, 1, 9, 2, 7]:
            h.inserir(v)
        self.assertEqual(h.remover_se(lambda x: x == 8), 8)
        self.assertEqual(h.listar_ordenado(), [1, 2, 3, 5, 7, 9])
        self.assertIsNone(h.remover_se(lambda x: x == 99))


class TestFila(unittest.TestCase):
    def test_prioridade_e_desempate(self):
        f = FilaAtendimento()
        f.registrar("Ana", 4)
        f.registrar("Bruno", 2)
        f.registrar("Carla", 2)
        f.registrar("Davi", 1)
        ordem = [f.chamar_proximo().nome for _ in range(4)]
        self.assertEqual(ordem, ["Davi", "Bruno", "Carla", "Ana"])
        self.assertIsNone(f.chamar_proximo())

    def test_reclassificar(self):
        f = FilaAtendimento()
        f.registrar("Ana", 5)
        f.registrar("Bruno", 3)
        f.reclassificar("p001", 1)
        self.assertEqual(f.proximo().nome, "Ana")

    def test_cancelar(self):
        f = FilaAtendimento()
        f.registrar("Ana", 3)
        f.registrar("Bruno", 3)
        self.assertEqual(f.cancelar("P001").nome, "Ana")
        self.assertEqual(f.tamanho(), 1)
        self.assertIsNone(f.cancelar("P999"))

    def test_validacoes(self):
        f = FilaAtendimento()
        with self.assertRaises(ValueError):
            f.registrar("  ", 3)
        with self.assertRaises(ValueError):
            f.registrar("Ana", 9)

    def test_serializacao(self):
        f = FilaAtendimento()
        f.registrar("Ana", 4)
        f.registrar("Bruno", 1)
        f2 = FilaAtendimento.de_dict(f.para_dict())
        self.assertEqual([p.nome for p in f2.listar()], ["Bruno", "Ana"])
        self.assertEqual(f2.registrar("Carla", 3).senha, "P003")


if __name__ == "__main__":
    unittest.main()
