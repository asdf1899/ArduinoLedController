#include <SoftwareSerial.h>
int rxPin= 0;
int txPin= 1;
SoftwareSerial BT(rxPin, txPin);

String message;
const int led = 11;
char m;
void setup() {
  Serial.begin(9600);
  BT.begin(9600);
  pinMode(led, OUTPUT);
}

void loop() {
  while(BT.available() > 0){
    m = BT.read();
    Serial.println(m);
  }
  if (!BT.available()){
    if (m == 'H'){
      digitalWrite(led, HIGH);
      Serial.println("Led on");
      delay(1000);
    }
    else if (message == 'L'){
      digitalWrite(led, LOW);
      Serial.println("Led off");
      delay(1000);
    }
  }
  delay(500);
}
