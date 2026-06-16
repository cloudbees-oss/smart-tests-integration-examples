use strict;
use warnings;
use utf8;
use Test2::V0;

use Example::Math;


is(Example::Math::add(1, 2), 5, "add(1, 2) == 5");

subtest 'add' => sub {
    my ($a, $b) = (1, 2);

    is(Example::Math::add($a, $b), 4, "$a + $b");
};
