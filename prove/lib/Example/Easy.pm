package Example::Easy;
use 5.008001;
use strict;
use warnings;
use utf8;

sub str_concat {
    my ($a, $b) = @_;
    sleep(1);

    return $a.$b;
}

1;