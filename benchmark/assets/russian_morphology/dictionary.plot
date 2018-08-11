set terminal pngcairo enhanced nocrop font "Verdana,10" fontscale 1.0 size 800, 800
set output 'plot.png'

set grid
set border 3 back lc rgb '#636363' lt 1
set tics nomirror

unset key
set style data linespoints
set xtics border in scale 1,0.5 nomirror  autojustify
set xtics norangelimit
set xtics ()

load 'encoders.pal'
unset colorbox

# Multi-plot Layout (0.5-0.5-1.0)
set multiplot
set bmargin 3
set tmargin 4

# Plot: mean words
set origin 0.5,0
set size 0.5,0.5
set title "Mean words with same code" font "Verdana,12"
plot for [i=2:5] 'mean.dat' using i:xtic(1) ls (i-1) title columnheader(i)

# Plot: max words
set origin 0,0
set size 0.5,0.5
set title "Max words with same code" font "Verdana,12"
set yrange [0:80000]
set ytics ("" 0, "10K" 10000, "20K" 20000,"30K" 30000,"40K" 40000,"50K" 50000, "60K" 60000,"70K" 70000,"80K" 80000)
plot for [i=2:5] 'max.dat' using i:xtic(1) ls (i-1) title columnheader(i)

# Plot: number of codes
set origin 0,0.5
set size 1,0.5
set tmargin 3
set bmargin 5
unset key
set key outside bottom center horizontal font "Verdana,10"
set title "Number of phonetic codes" font "Verdana,12"
set yrange [0:2000000]
set ytics ("" 0, "200K" 200000, "400K" 400000, "600K" 600000,"800K" 800000,"1M" 1000000,"1.2M" 1200000, "1.4M" 1400000, "1.6M" 1600000, "1.8M" 1800000, "2M" 2000000)
plot for [i=2:8] 'codes.dat' using i:xtic(1) ls (i-1) title columnheader(i)

unset multiplot