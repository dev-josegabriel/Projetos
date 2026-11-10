"""Min-Heap implementada do zero (sem heapq).

Uma heap é uma árvore binária guardada num array:
  - pai de i:        (i - 1) // 2
  - filho esquerdo:  2 * i + 1
  - filho direito:   2 * i + 2
Regra: todo pai é MENOR (ou igual) aos filhos, então o menor sempre fica na raiz.

Complexidade:
  inserir ........ O(log n)
  espiar ......... O(1)
  remover_topo ... O(log n)
  remover_se ..... O(n) para achar + O(log n) para reorganizar
"""


class MinHeap:
    def __init__(self):
        self._dados = []

    def __len__(self):
        return len(self._dados)

    def esta_vazia(self):
        return len(self._dados) == 0

    def inserir(self, item):
        self._dados.append(item)
        self._subir(len(self._dados) - 1)

    def espiar(self):
        """Devolve o menor item sem remover (ou None se vazia)."""
        return self._dados[0] if self._dados else None

    def remover_topo(self):
        """Remove e devolve o menor item (ou None se vazia)."""
        if not self._dados:
            return None
        topo = self._dados[0]
        ultimo = self._dados.pop()
        if self._dados:
            self._dados[0] = ultimo
            self._descer(0)
        return topo

    def remover_se(self, predicado):
        """Remove o primeiro item que satisfaz o predicado (ou None)."""
        for i, item in enumerate(self._dados):
            if predicado(item):
                ultimo = self._dados.pop()
                if i < len(self._dados):
                    self._dados[i] = ultimo
                    # o item trocado pode precisar subir OU descer
                    self._subir(i)
                    self._descer(i)
                return item
        return None

    def buscar(self, predicado):
        for item in self._dados:
            if predicado(item):
                return item
        return None

    def listar_ordenado(self):
        """Lista em ordem de saída, sem alterar a heap original."""
        copia = MinHeap()
        copia._dados = list(self._dados)
        resultado = []
        while not copia.esta_vazia():
            resultado.append(copia.remover_topo())
        return resultado

    def itens(self):
        """Itens na ordem interna do array (não ordenada)."""
        return list(self._dados)

    # ---------- auxiliares ----------
    def _subir(self, i):
        while i > 0:
            pai = (i - 1) // 2
            if self._dados[i] < self._dados[pai]:
                self._dados[i], self._dados[pai] = self._dados[pai], self._dados[i]
                i = pai
            else:
                break

    def _descer(self, i):
        n = len(self._dados)
        while True:
            esq, dir_ = 2 * i + 1, 2 * i + 2
            menor = i
            if esq < n and self._dados[esq] < self._dados[menor]:
                menor = esq
            if dir_ < n and self._dados[dir_] < self._dados[menor]:
                menor = dir_
            if menor == i:
                break
            self._dados[i], self._dados[menor] = self._dados[menor], self._dados[i]
            i = menor
