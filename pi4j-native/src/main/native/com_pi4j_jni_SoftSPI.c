#include <errno.h>
#include <stdio.h>
#include <stdint.h>
#include <time.h>
#include <unistd.h>

#include <wiringPi.h>
#include <jni.h>

static inline void nsleep(long nanos)
{
  int r;
  struct timespec tv;
  tv.tv_sec = 0;
  tv.tv_nsec = nanos;

  do {
    r = nanosleep(&tv, &tv);
  } while((r==-1) && (errno == EINTR));
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
  return JNI_VERSION_1_2;
}

JNIEXPORT jbyteArray JNICALL Java_com_pi4j_jni_SoftSPI_readWrite(JNIEnv * env, jclass class, jint cs, jint clk, jint mosi, jint miso, jlong speed, jboolean cpol, jboolean cpha, jbyteArray data)
{
  int i;
  long nanos = 500000000 / speed;
  jsize j;
  jbyte in, out;

  jsize arraySize = (*env)->GetArrayLength(env, data);
  jbyte* outputData = (*env)->GetByteArrayElements(env, data, JNI_FALSE);

  jbyteArray input = (*env)->NewByteArray(env, arraySize);
  jbyte* inputData = (*env)->GetByteArrayElements(env, input, JNI_FALSE);

  //Assert Chip Select
  digitalWrite(cs, LOW);

  for(j = 0; j < arraySize; j++)
  {
    in = 0;
    out = outputData[j];

    for(i = 0; i < 8; i++) {
      if(!cpha)
	digitalWrite(mosi, (out & 0x80) >> 7); //Write current bit

      //Assert Clock
      digitalWrite(clk, !cpol);
      nsleep(nanos);
      
      if(cpha)
	digitalWrite(mosi, (out & 0x80) >> 7); //Write current bit
      else
	in = (in << 1) | digitalRead(miso); //Read current bit
      
      //Deassert Clock
      digitalWrite(clk, cpol);
      nsleep(nanos);
      
      if(cpha)
	in = (in << 1) | digitalRead(miso); //Read current bit
            
      out <<= 1;
    }

    inputData[j] = in;
  }

  //Deassert Chip Select
  digitalWrite(cs, HIGH);

  (*env)->ReleaseByteArrayElements(env, data, outputData, 0);
  (*env)->ReleaseByteArrayElements(env, input, inputData, 0);
  return input;
}
