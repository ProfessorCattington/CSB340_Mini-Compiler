    4      1 Identifier      n
    4      3 Op_assign      
    4      5 Integer           100
    4      8 Semicolon      
    5      1 Identifier      sum
    5      5 Op_assign      
    5      7 Integer             0
    5      8 Semicolon      
    6      1 Keyword_while  
    6      7 LeftParen      
    6      8 Identifier      n
    6     10 Op_greater     
    6     12 Integer             0
    6     13 RightParen     
    6     15 LeftBrace      
    8      2 Identifier      sum
    8      6 Op_assign      
    8      8 Identifier      sum
    8     12 Op_add         
    8     14 LeftParen      
    8     15 Identifier      n
    8     17 Op_multiply    
    8     19 LeftParen      
    8     20 Identifier      n
    8     22 Op_subtract    
    8     24 Integer             1
    8     25 RightParen     
    8     26 RightParen     
    8     27 Semicolon      
    9      2 Identifier      n
    9      4 Op_assign      
    9      6 Identifier      n
    9      8 Op_subtract    
    9     10 Integer             1
    9     11 Semicolon      
   10      1 RightBrace     
   11      1 Keyword_print  
   11      6 LeftParen      
   11      7 String          "The n factorial of n = 100 is : "
   11     41 RightParen     
   11     42 Semicolon      
   12      1 Keyword_print  
   12      6 LeftParen      
   12      7 Identifier      sum
   12     10 RightParen     
   12     11 Semicolon      
   13      1 End_of_input   