import pytest


class TestDeps:
    set_this_thing = 0

    # --- Independent tests for Launchable to potentially remove ---
    def test_independent_a(self):
        """This test has no dependencies or ordering."""
        assert True

    def test_independent_b(self):
        """This test also has no dependencies or ordering."""
        assert "hello" == "hello"
    # -------------------------------------------------------------

    @pytest.mark.order(1)
    def test_dep_1(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 1

    @pytest.mark.order(2)
    def test_dep_2(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 2

    @pytest.mark.order(3)
    def test_dep_3(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 3

    @pytest.mark.order(4)
    def test_dep_4(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 4

    @pytest.mark.order(5)
    @pytest.mark.dependency()
    def test_dep_5(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 5

    @pytest.mark.dependency(depends=["TestDeps::test_dep_5"])
    def test_dep_6(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 6

    @pytest.mark.dependency(depends=["TestDeps::test_dep_6"])
    def test_dep_7(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 7

    @pytest.mark.dependency(depends=["TestDeps::test_dep_7"])
    def test_dep_8(self):
        TestDeps.set_this_thing += 1
        assert TestDeps.set_this_thing == 8