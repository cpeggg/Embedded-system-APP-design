#include <stdio.h>
int a(){
  return 1;
}
int b(){
  return 2;
}
int c(){
  return 3;
}int d(){
  return 4;
}
/*
  Please upload one function to this file using git command so that we can finish this program.
*/
int main(){
  int i;
  i=2018;
  i+=a();
  i+=b();
  i+=c();
  i+=d();
  printf("%d",i);
  return 0;
}
//Nice to meet you.
