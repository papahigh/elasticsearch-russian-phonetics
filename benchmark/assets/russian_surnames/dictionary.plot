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
set title "Mean names with same code" font "Verdana,12"
plot for [i=2:5] 'mean.dat' using i:xtic(1) ls (i-1) title columnheader(i)

# Plot: max words
set origin 0,0
set size 0.5,0.5
set yrange [0:1200]
set ytics ("" 0, 200, 400, 600, 800,"1K" 1000, "1.2K" 1200)
set title "Max names with same code" font "Verdana,12"
plot for [i=2:5] 'max.dat' using i:xtic(1) ls (i-1) title columnheader(i)

# Plot: number of codes
set tmargin 3
set bmargin 5
set origin 0,0.5
set size 1,0.5
unset key
set yrange [0:250000]
set ytics ("" 0, "50K" 50000, "100K" 100000, "150K" 150000,"200K" 200000,"250K" 250000)
set key outside bottom center horizontal font "Verdana,10"
set title "Number of phonetic codes" font "Verdana,12"
plot for [i=2:8] 'codes.dat' using i:xtic(1) ls (i-1) title columnheader(i)

unset multiplot