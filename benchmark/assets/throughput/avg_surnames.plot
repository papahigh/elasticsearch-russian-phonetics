set terminal pngcairo enhanced nocrop font "Verdana,10" fontscale 1.0 size 800, 600
set output 'avg_surnames.png'

load 'it.pal'
unset colorbox

set title "Average encoding throughput, ops/sec (Russian names)" font ",14"
set tmargin 5
set bmargin 7

set grid
set border 3 back lc rgb '#636363' lt 1
set tics nomirror

set style data histograms
set boxwidth 0.75
set style fill solid 1 border -1

set xtics nomirror rotate by -45

set yrange [0:4000000]
set ytics ("" 0, "500K" 500000, "1M" 1000000, "1.5M" 1500000, "2M" 2000000, "2.5M" 2500000,"3M" 3000000,"3.5M" 3500000,"4M" 4000000)


plot './avg_surnames.dat' using ($0):2:($3):xtic(1) notitle with boxes palette;
