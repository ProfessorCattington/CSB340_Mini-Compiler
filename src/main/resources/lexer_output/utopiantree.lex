    3      1 Identifier      output
    3      8 Op_assign      
    3     10 Integer             1
    3     11 Semicolon      
    5      1 Keyword_if     
    5      4 LeftParen      
    5      5 Identifier      n
    5      7 Op_equal       
    5     10 Integer             0
    5     11 RightParen     
    6      1 LeftBrace      
    7      1 RightBrace     
    8      1 Keyword_else   
    9      1 LeftBrace      
   10      2 Identifier      i
   10      4 Op_assign      
   10      6 Integer             1
   10      7 Semicolon      
   11      5 Keyword_while  
   11     11 LeftParen      
   11     12 Identifier      i
   11     14 Op_less        
   11     16 Identifier      n
   11     18 Op_add         
   11     20 Integer             1
   11     21 RightParen     
   12      5 LeftBrace      
   13      3 Identifier      i
   13      5 Op_assign      
   13      7 Identifier      i
   13      9 Op_add         
   13     11 Integer             1
   13     12 Semicolon      
   15      9 Keyword_if     
   15     12 LeftParen      
   15     13 Identifier      i
   15     15 Op_mod         
   15     17 Integer             2
   15     19 Op_notequal    
   15     22 Integer             0
   15     23 RightParen     
   16      9 LeftBrace      
   17     13 Identifier      output
   17     20 Op_assign      
   17     22 Identifier      output
   17     29 Op_multiply    
   17     31 Integer             2
   17     32 Semicolon      
   18      9 RightBrace     
   19      9 Keyword_else   
   19     14 Keyword_if     
   19     17 LeftParen      
   19     18 Identifier      i
   19     20 Op_mod         
   19     22 Integer             2
   19     24 Op_equal       
   19     27 Integer             0
   19     28 RightParen     
   20      9 LeftBrace      
   21     13 Identifier      output
   21     20 Op_assign      
   21     22 Identifier      output
   21     29 Op_add         
   21     31 Integer             1
   21     32 Semicolon      
   22      9 RightBrace     
   23      5 RightBrace     
   24      1 RightBrace     
   26      1 Keyword_print  
   26      6 LeftParen      
   26      7 Identifier      output
   26     13 RightParen     
   26     14 Semicolon      
   27      1 End_of_input   