set terminal pngcairo enhanced nocrop font "Verdana,10" fontscale 1.0 size 800, 600
set output 'plot.png'

load 'encoders.pal'
unset colorbox

set title "Misspellings and typos matching" font ",14"
set tmargin 5
set bmargin 7

set grid
set border 3 back lc rgb '#636363' lt 1
set tics nomirror

set style data histograms
set boxwidth 0.75
set style fill solid 1 border -1

set yrange [0:90972]
set ytics ("" 0, "10K" 10000, "20K" 20000,"30K" 30000,"40K" 40000,"50K" 50000,"60K" 60000,"70K" 70000,"80K" 80000,"90K" 90000)

set xtics nomirror rotate by -45


plot './results.dat' using ($0):3:($4):xtic(1) notitle with boxes palette,  \
      './results.dat'  using ($0):3:(sprintf("%d%",$2)) notitle with labels offset char 0,1 ;