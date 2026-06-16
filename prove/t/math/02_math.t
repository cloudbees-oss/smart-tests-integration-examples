use strict;
use warnings;
use utf8;
use Test2::V0;

use Example::Math;


is(Example::Math::add(2, 1), 3, "add(2, 1) == 3");

subtest 'add_v2' => sub {
    my ($a, $b, $c, $d) = (1, 2, 3, 4);

    is(Example::Math::add($a, $b), 3, "$a + $b");
    is(Example::Math::add($c, $d), 7, "$c + $d");
};

subtest 'double_v2' => sub {
    my ($a, $b) = (1, 3);

    is(Example::Math::double($a), 2, "$a + 2");
    is(Example::Math::double($b), 6, "$b + 2");
};

is(Example::Math::add(2, 3), 5, "add(2, 3) == 5");

done_testing;
