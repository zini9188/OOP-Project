# Project
해당 프로젝트는 객체지향언어2 수업에서 Java 실습으로 진행한 1인 텀프로젝트입니다.

- audio 파일에 음원을 추가하여 배경음을 삽입할 수 있습니다.

- GameTitleFrame, GamePanel 클래스의 loadAudio
	```
    private void loadAudio() {
        try {
            clip = AudioSystem.getClip();
            File audioFile = new File("audio/원하는 음원 파일의 이름");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(audioStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
	```


## 개요

랜덤한 단어를 갖는 미사일이 땅에 떨어지는 것을 막기 위해 해당 단어를 맞춰야 한다.

라운드는 1단계부터 시작하며 0초까지 버티면 다음 라운드로 넘어갈 수 있다.

## 기능

- 난이도
  - 쉬움: 미사일 생성 시간이 5초이고 5ms 속도로 떨어진다.
  - 보통: 미사일 생성 시간이 3초이고 10ms 속도로 떨어진다.

  - 어려움: 미사일 생성 시간이 1초이고 15ms 속도로 떨어진다.

- 아이템
  - 보라색 미사일: 떨어지는 미사일의 속도가 0.25배로 줄어들고 서서히 원래 속도로 증가한다.
  - 빨간색 미사일: 10초간 점수가 두배가 된다.
  
  - 초록색 미사일: 사용자의 생명력이 1-4인 경우 생명력을 회복한다.

- 랭킹
- 단어 추가
- 단어장
- 음소거
- 게임 재시작

