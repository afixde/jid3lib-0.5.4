:
FILES=*.mp3
DBFILES=/d/Studio/Musik/_Stream/dblist.txt
#echo " Test: FILES=$FILES."
for I in $FILES; do
  #echo " Test: I=$I."
  CMP=`echo "$I" | sed 's/\.mp3//Ig' | sed "s/[_',]\+//g" | sed 's/[ -/YyJj0-9]\+/\.\*/g' | sed 's/[(].*[)]//g' | sed 's/feat/\.\*/Ig'`
  # echo " Test: CMP=$CMP."
  if grep -qis "$CMP" $DBFILES; then
	echo "mv \"$I\" _doubles"
  else
	# echo " Test: ok \"$I\""
	:
  fi
done



