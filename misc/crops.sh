TARGET="./menu/"
SOURCE="./bigframes/menu/"
# echo "nightfren"
# rm ./nightfren/*
# ls ./bigframes/nightfren/*
# mogrify \
#   -gravity Center \
#   -crop 520x250-570-635 \
#   +repage \
#   -path ./nightfren/ \
#   "./bigframes/nightfren/*.png" \

# echo "pestbirb"
# rm ./pestbirb/*
# ls ./bigframes/pestbirb/*
# mogrify \
#   -gravity Center \
#   -crop 800x1100+225+250 \
#   +repage \
#   -path ./pestbirb/ \
#   "./bigframes/pestbirb/*.png" \

# echo "skeletodd"
# rm ./skeletodd/*
# ls ./bigframes/skeletodd/*
# mogrify \
#   -gravity Center \
#   -crop 1035x880-350+243 \
#   +repage \
#   -path ./skeletodd/ \
#   "./bigframes/skeletodd/*.png" \


# TARGET="./menu/"
# SOURCE="./bigframes/menu/"
# echo "title"
# rm "${TARGET}*"
# ls $SOURCE
#  mogrify \
#    -gravity Center \
#    -crop 700x300-0-850 \
#    +repage \
#    -path $TARGET \
#    "${SOURCE}*.png" \

# TARGET="./title/"
# SOURCE="./bigframes/menu/"
# echo "papyr"
# rm "${TARGET}*"
# ls $SOURCE
#  mogrify \
#    -gravity Center \
#    -crop 1370x450-0-490 \
#    +repage \
#    -path $TARGET \
#    "${SOURCE}*.png" \

# TARGET="./background/"
# SOURCE="threescreens.jpg"
# mogrify \
#    -gravity Center \
#    -crop 2049x2049 \
#    +repage \
#    -path $TARGET \
#    "${SOURCE}" \

# TARGET="./ui/spider"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 200x200-625-130 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png" \

# TARGET="./ui/hand/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 450x585-745+555 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png" \

# TARGET="./ui/options/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 550x149-1940-535 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/sounds0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/sounds0001.png"

# TARGET="./ui/options/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 450x149-1980-368 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/music0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/music0001.png"

# TARGET="./ui/options/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 870x149-1770-238 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/fullscreen0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/fullscreen0001.png"

# TARGET="./ui/options/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 750x149-1795-120 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/windowed0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/windowed0001.png"

# TARGET="./ui/options/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 630x290-2175-970 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/options0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/options0001.png"

# TARGET="./ui/games/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 1900x370+2050-710 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/dodge0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/dodge0001.png"

# TARGET="./ui/games/"
# SOURCE="./bigframes/ui/"
# mogrify \
#    -gravity Center \
#    -crop 920x730+1965-145 \
#    +repage \
#    -path "${TARGET}" \
#    "${SOURCE}*.png"
#
# mv "${TARGET}/frame0000.png" "${TARGET}/botd0000.png"
# mv "${TARGET}/frame0001.png" "${TARGET}/botd0001.png"

TARGET="./ui/games/"
SOURCE="./bigframes/ui/"
mogrify \
   -gravity Center \
   -crop 1660x290+1970+570 \
   +repage \
   -path "${TARGET}" \
   "${SOURCE}*.png"

mv "${TARGET}/frame0000.png" "${TARGET}/escape0000.png"
mv "${TARGET}/frame0001.png" "${TARGET}/escape0001.png"
