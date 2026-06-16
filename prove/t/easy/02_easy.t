use strict;
use warnings;
use utf8;
use Test2::V0;

use Example::Easy;

is(Example::Easy::str_concat("b", "a"), "ba", "b + a = ba");

subtest 'str_concat_v2' => sub {
    my ($a, $b, $c, $d) = ("a", "bb", "ccc", "dddd");

    is(Example::Easy::str_concat($a, $d), "adddd", "$a + $d");
    is(Example::Easy::str_concat($b, $c), "bbccc", "$b + $c");
};

done_testing;

