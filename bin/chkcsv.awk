BEGIN{FS=","; idx=0}
{
	if (NR == 1)
	{
		# column titles
		for (i = 1; i <= NF; i++)
		{
			#printf("Test: i = %d, %s\n", i, $i);
            if(match($i, "Interpret"))
				art = i;
			else if(match($i, "Titel"))
				tit = i;
			else if(match($i, "Album"))
				alb = i;
			else if(match($i, "Länge"))
				lan = i;
			else if(match($i, "Jahr"))
				jah = i;
			else if(match($i, "Bewertung"))
				rat = i;
			else if(match($i, "Bitrate"))
				bit = i;
			else if(match($i, "Pfad"))
				pth = i;
			#else
			#	print("ERROR unknown field: [%s]", $i);
		}
	}
	else if (int($rat) > 50)
	{
		# build key
		key1 = tolower($tit);
		key2 = tolower($art);
		lans = $lan;
	    gsub("[\:\"]", "", lans);
		#printf("Test: 1 lan = %s, lans = [%s]\n", $lan, lans);
		key3 = int(lans);
		key3 = key3 - key3 % 10;
		#printf("Test: 2 key3 = [%d]\n", key3);
		key = sprintf("%s~%s~%d", key1, key2, key3);
		# save
		bit1 = int(mp3_bit[key]);
		bit2 = int($bit);
		if(bit2 > bit1)
		{
			if(bit1 > 0)
			{
				#printf("Test: %s (%d < %d) replaced [%s]  by  [%s]\n", key, bit1, bit2, mp3_pth[key], $pth); 
			}
			#printf("Test: idx = %d, %s [%s]\n", idx, key, $pth);
			mp3_tit[key] = $tit;
			mp3_int[key] = $art;
			mp3_alb[key] = $alb;
			mp3_lan[key] = $lan;
			mp3_rat[key] = $rat;
			mp3_bit[key] = $bit;
			mp3_pth[key] = $pth;
			
			mp3_key[idx] = key;
			idx++;
		}
	}
}
END {
    for (i = 0; i < idx; i++)  {
        key=mp3_key[i];
		printf("rem %3d %s\t%2d, %3d, [%s]\n", i, mp3_key[i], mp3_rat[key], mp3_bit[key], mp3_pth[key]);
		printf("copy %s tmp\n", mp3_pth[key]);
	}
    
}
