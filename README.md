# Welcome to @Ah_Huat_Bot
This runs a telegram bot and a web service as well. The web service has nothing much to it other than an endpoint to keep the service alive. The bot webscraps data and pushes messages to subscribers every Monday and Thursday after 6:30pm.

# The bot
[Telegram @ah_huat_bot](https://t.me/ah_huat_bot)

## Commands

| Command | Description |
| --- | --- |
| /subscribe |Subscribe to all draw releases |
| /subscribe 1000000 |Subscribe to all draw with a minimum value |
| /next |Get date, time, and value of the next draw |
| /prev |Get date, time, value, winning groups, and winning numbers |
| /calculate 1,5,6,13,28,35 | Calculate winnings |

# Condition
Condition for for receiving alerts:
- Data exists and is more than a certain value
    - Monday and Thursday after 6:30pm or everyday at 11am until data expires (after aforementioned date time)
    

# Telegram Java API by Rubenlagus
[https://github.com/rubenlagus/TelegramBots](https://github.com/rubenlagus/TelegramBots)