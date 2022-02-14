# My1

Przedmiotem pracy jest projekt Robota kuchennego sterowanego za pomocą aplikacji mobilnej, który ma za zadanie automatycznie przygotowywać posiłki. Prezentowany system ma zwolnić człowieka z obowiązku przygotowywania jedzenia oraz pomóc w monotonnych czynnościach podczas przyrządzania posiłków. Projekt składa się z zrobotyzowanego ramienia antropomorficznego, którego zadaniem jest wykonywanie różnego rodzaju dań lub operacji, aplikacji mobilnej napisanej dla użytkownika systemu Android, za pomocą której robot jest sterowany oraz z funkcji w środowisku IDE Arduino z których użytkownik tworzy algorytm pracy, poprzez odpowiednie ich ustawienie. Komunikacja pomiędzy aplikacją a urządzeniem odbywa się za pośrednictwem kanału Bluetooth. W środowisku GitHub umieszczono jedynie kod źódłowy aplikacji mobilnej do sterowania robota kuchennego zaprojektowanego w ramach pracy inżynierskiej. 

## Krótki opis aplikacji

Ekran startowy aplikacji umożliwiający wyszukiwanie pobliskich urządzeń Bluetooth i wypisuje ich adresy MAC w liście

![image](https://user-images.githubusercontent.com/58587279/153871079-16948b4a-c9da-47f5-8a4e-a22ee640fa30.png)

Do uzyskania komunikacji między urządzeniami użyty został protokół RFCOMM (Radio Frequency Communication). Jest on protokołem, który emuluje zachowanie portu szeregowego. Jego zaletą jest kompatybilność z rozwiązaniem RS-232, ponieważ pozwala to na zastosowanie RFCOMM do istniejących już rozwiązań bez zmian w programie. Wykorzystywany system może być współdzielony przez wiele aplikacji, z powodu wykorzystania zwielokrotnienia łączy zwanych kanałami. Dlatego też łączność taka może odbywać się jednocześnie poprzez wiele kanałów, którymi odpowiednio można sterować. 

![image](https://user-images.githubusercontent.com/58587279/153871144-b6419dd4-dd07-46af-a0b6-253de59135b9.png)

W tym samym czasie w tle nastąpi inicjacja komunikacji oraz połączenie z urządzeniem zewnętrznym. Należy podkreślić, że zadaniem aplikacji będzie wysyłanie parametrów nastawczych do urządzenia zewnętrznego, dlatego też wykorzystane zostanie połączenie, które ogranicza zastosowane funkcje do minimum. Przy tym rozwiązaniu konieczne jest skorzystanie z instancji klasy BluetoothSocket(). Jest to reprezentacja interfejsu gniazda Bluetooth, który pozwala aplikacji na wymianę danych z innym urządzeniem Bluetooth. Aby możliwa była poprawna komunikacja pomiędzy dwoma urządzeniami wykorzystany został identyfikator UUID. Unikatowy identyfikator uniwersalny ( UUID )  jest to128-bitowy numer używany do identyfikacji informacji w systemach komputerowych. Wykorzystanie identyfikatora UUID polega na tym, że jest on wystarczająco duży, aby można było wybrać dowolny losowy identyfikator, niekolidujący z żadnym innym identyfikatorem. W takim przypadku służy do jednoznacznego rozpoznania usługi Bluetooth aplikacji. 

Aby zasugerować użytkownikowi pewną potrawę, w aplikacji została zastosowana akcja otwierania strony internetowej po dłuższym przytrzymaniu guzika z odpowiednią ikoną dania. Przykładowe otwarcie strony www.domowe-wypieki.pl z przepisem na omlet na słodko z malinami zostanie zrealizowane po przytrzymaniu ikonki symbolizującej deser w aplikacji i wygląda następująco:

![image](https://user-images.githubusercontent.com/58587279/153871327-41975ce5-4bd8-400f-808a-d5ee6db7c28f.png)

W tej pracy przedstawione zostały tylko najbardziej kluczowe fragmenty użytkowe całego kodu aplikacji. Pliki w języku XML odpowiadające za część graficzną projektu zostały pominięte z powodu zbyt małego znaczenia merytorycznego. Ograniczenie kodu pozwoliło zachować przejrzystość pracy oraz pokazać kluczowe fragmenty. W pliku zastosowano szereg animacji, aby podnieść atrakcyjność projektu. Całościowy wygląd kluczowej aktywności aplikacji wygląda następująco:

![image](https://user-images.githubusercontent.com/58587279/153871372-ad8db7d5-978d-4c53-a85f-1ae4cb75c46f.png)




