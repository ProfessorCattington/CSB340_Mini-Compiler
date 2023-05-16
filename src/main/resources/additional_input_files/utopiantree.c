/* "utopian tree" */

output = 1;

if (n == 0) 
{
}
else
{
	i = 1;
    while (i < n + 1)
    {
		i = i + 1;
		
        if (i % 2 != 0)
        {
            output *= 2;
        }
        else if (i % 2 == 0)
        {
            output += 1;
        }
    }
}

print(output);