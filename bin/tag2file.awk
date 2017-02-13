function stripVal(key, inp)
{
    ret = "";
    for (i = 3; i <= NF; i++)
    {
        if (match($i, "^\[.*"))
            break;
        tmp = ret;
        ret = sprintf("%s %s", tmp, $i);
    }
    ret = substr(ret, 2);
    
    printf("Test: %s = [%s]\n", key, ret);
    
    if (ret ~ cntrl_regexp)
        return "";
        
    return ret;
}

BEGIN{mp3tags = 0; mp3file = 0; count = 0; cntrl_regexp = "[[:cntrl:]]+"}
{
    if (mp3tags == 0)
    {
        if (match($0, "^MP3 Tags"))
        {
            mp3tags = 1;
            mp3file = 1;
        }
    }
    else
    {
        if (NF < 1)
        {
            ;
        }
        else if (match($0, "^ \-.*mp3$"))
        {
            fname = substr($0, 4);
            printf("\nTest: fname = [%s]\n", fname);
            mp3file = 0;
        }
        else if (match($2, "SongTitle\:"))
        {
            SongTitle = stripVal("SongTitle", $0);
        }
        else if (match($2, "LeadArtist\:"))
        {
            LeadArtist = stripVal("LeadArtist", $0);
        }
        else if (match($2, "AlbumTitle\:"))
        {
            AlbumTitle = stripVal("AlbumTitle", $0);
        }
        else if (match($2, "SongGenre\:"))
        {
            SongGenre = stripVal("SongGenre", $0);
        }
        else if (match($2, "Year\:"))
        {
            Year = stripVal("Year", $0);
        }
        else if (match($2, "TrackNo\:"))
        {
            TrackNo = stripVal("TrackNo", $0);
            newFname = sprintf("%s.%s - %s.mp3", TrackNo, LeadArtist, SongTitle);
            if (!match(newFname, "\\?"))
            {
                printf("copy \"%s\" \"~%s\"\n", fname, newFname);
            }
        }
    }
}
