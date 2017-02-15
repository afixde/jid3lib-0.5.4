:
FILES=*.mp3
#echo " Test: FILES=$FILES."
for I in $FILES; do
  #echo " Test: I=$I."
  CMP=`echo "$I" | sed 's/\.mp3//Ig' | sed 's/[_0-9]\+//g' | sed 's/[ -&+]\+/\.\*/g' | sed 's/[(].*[)]//g'`
  # echo " Test: CMP=$CMP."
  if grep -qis "$CMP" ../list.txt; then
	echo "mv \"$I\" _doubles"
  else
	# echo " Test: ok \"$I\""
	:
  fi
done



