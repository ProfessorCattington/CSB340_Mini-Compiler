/**
 * n factorial.
 */
n = 100;
sum = 0;
while (n > 0) {

	sum = sum + (n * (n - 1));
	n = n - 1;
}
print("The n factorial of n = 100 is : ");
print(sum);