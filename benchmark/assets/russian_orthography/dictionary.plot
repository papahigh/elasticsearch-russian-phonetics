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
set yrange [0:3500]
set ytics ("" 0, 500,"1K" 1000,"1.5K" 1500,"2K" 2000, "2.5K" 2500, "3K" 3000, "3.5K" 3500)
set title "Max words with same code" font "Verdana,12"
plot for [i=2:5] 'max.dat' using i:xtic(1) ls (i-1) title columnheader(i)

# Plot: number of codes
set tmargin 3
set bmargin 5
set origin 0,0.5
set size 1,0.5
unset key
set yrange [0:160000]
set ytics ("" 0, "20K" 20000,"40K" 40000,"60K" 60000,"80K" 80000, "100K" 100000, "120K" 120000, "140K" 140000, "160K" 160000)
set key outside bottom center horizontal font "Verdana,10"
set title "Number of phonetic codes" font "Verdana,12"
plot for [i=2:8] 'codes.dat' using i:xtic(1) ls (i-1) title columnheader(i)

unset multiplot