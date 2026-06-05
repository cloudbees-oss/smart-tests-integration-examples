class Calculator(object):
    def __init__(self):
        self._result = 0

    def plus(self, a: int):
        self._result = self._result + a

    def minus(self, a: int):
        self._result = self._result - a

    def result_is(self, a: int):
        if self._result != a:
            raise AssertionError("{} != {}".format(self._result, a))
