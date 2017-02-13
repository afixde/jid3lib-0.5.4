BEGIN{NumSep=" - ";ArtSep=" - ";TitSep=".mp3"; idx=0}
{
    fname = $0;
    wname = fname;
    mp3_File[idx++]=fname;
    offs=0;
    if (NumSep != "") {
        len=index(wname, NumSep);
        if (len > 0) {
            Number=substr(wname, offs, len-1);
            mp3_Number[fname]=Number;
            wname=substr(wname, offs + len + length(NumSep));
        }
    }
    if (ArtSep != "") {
        len=index(wname, ArtSep);
        if (len > 0) {
            Artist=substr(wname, offs, len-1);
            mp3_Artist[fname]=Artist;
            wname=substr(wname, offs + len + length(ArtSep));
        }
    }
    if (TitSep != "") {
        len=index(wname, TitSep);
        if (len > 0) {
            Title=substr(wname, offs, len-1);
            mp3_Title[fname]=Title;
            wname=substr(wname, offs + len + length(TitSep));
        }
    }
}
END {
    for (i = 0; i < idx; i++)  {
        file=mp3_File[i];
        #printf (" - [%02d] %s\n\tNumber = %s\n\tArtist = %s\n\tTitle = %s\n\n", i, file, mp3_Number[file], mp3_Artist[file], mp3_Title[file]);
        printf ("call mp3tag -t \"%s\" -i \"%s\" -n \"%s\"  \"%s\"\n", mp3_Title[file], mp3_Artist[file], mp3_Number[file], file);
    }
    
}
