# Auction!

> *minecraft auction plugin*
>

## Todo

- ๊ธฐ๋ณธ์ ์ธ Auction!
  - ๐ฐ์ฌํ MONEY!
  - **๐ค**์์ดํ ๊ตฌ๋งค & ํ๋งค
  - ๐์์ธ ๊ฒ์
  - โค๏ธ์ฅ๋ฐ๊ตฌ๋

## ๊ตฌํ

- ์ฌํ & ์์ดํ ์ ์ฅ
  - ์ฌํ๋ฅผ ์ ์ฅํ๋ ๋ฐฉ๋ฒ์ ๊ต์ฅํ ๋ง์์ต๋๋ค.
    - yaml ํ์ผ์ ์ฅ
    - nbt
  - ํ์ง๋ง ๊ทธ๋ด๊ฑฐ ์๋ค ๋ฐ๋ก DB๋๋ ค๋ฐ๊ธฐ
  - JDBC ์ฌ์ฉํ๊ธฐ build.gradle.kts

    ```kotlin
    repository {
        mavenCentral()
    }
    
    dependencies {
        implementation("mysql:mysql-connector-java:8.0.28")
    }
    ```

  ~~ํํํณ~~

  - ์ดํ object AuctionDatabase์์ DB์ ํต์ ํ๋ฉด ๋๋ค

    ```kotlin
    object AuctionDatabase {
        private lateinit var plugin: JavaPlugin
    
        //MySql configuration
        private lateinit var host: String
        private lateinit var port: String
        private lateinit var database: String
        private lateinit var user: String
        private lateinit var password: String
    
        fun init(
                plugin: JavaPlugin,
                host: String,
                port: String,
                database: String,
                user: String,
                password: String
        ) {
            this.plugin = plugin
        }
    
        fun getConnection() : Connection {
            Class.forName("com.mysql.cj.jdbc.Driver")
    
            val url = "jdbc:mysql://$host:$port/$database"
            val connection = DriverManager.getConnection(url, user, password)
    
            require(connection != null)
            return connection
        }
    
        inline fun connection(block: Connect.() -> Unit) {
            val conn = getConnection()
    		
            Connect(conn).block()
    		
            conn.close()
        }
    
        class Connect(val connection: Connection)
    }
    ```

  ~~์ฌ์ค DB ์จ๋ณด๊ณ  ์ถ์๋๊ฑด ์๋น๋ฐ~~

- ์์ดํ ํ๋งค& ๊ตฌ๋งค
  - [monun](http://github.com/monun)์ invfx์ kommand๋ฅผ ์ฌ์ฉํ๋ค.

    ```kotlin
    AuctionDatabase.connection {
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery("SELECT number, itemStack, prise, owner from items")
    	
        val list = ArrayList<AuctionItem>()
        while(rs.next()) {
            val number = rs.getInt("number")
            val itemStack = ItemStack.deserializeBytes (
                rs.getBlob("itemStack").run { getBytes(1, length().toInt()) }
            )
            val prise = rs.getInt("prise")
            val owner = UUID.fromString(rs.getString("owner"))
    
            list += AuctionItem(number, itemStack, prise, owner)
        }
    }
    ```

  - ์ด๋ ๊ฒ ์์ดํ๋ค์ ๊ฐ์ ธ์ฌ ์ ์๊ณ  select ๋ฌธ์ where ์ ์ฌ์ฉํ์ฌ ํํฐ๋ ์ฌ์ฉํ  ์ ์๋ค.
- ์ฅ๋ฐ๊ตฌ๋
  - DB ํ์ด๋ธ์ shopping_bag ํ์ด๋ธ์ ์ถ๊ฐ, ์ด ํ์ด๋ธ์์ ๋ฌด์จ ์์ดํ์ด ๋๊ตฌ์ ์ฅ๋ฐ๊ตฌ๋์ ๋ค์ด์๋์ง ํ์ธํ์ฌ ํ์ํ๋ค
  - TODO ์ฅ๋ฐ๊ตฌ๋์์ ์์ดํ ์ญ์  & item ํ์ด๋ธ์์ ์ญ์ ๋ ๊ฐ์ ๊ฐ์ด ์ญ์   (user ํฌํจ)
    - foreign key ์ฌ์ฉํ๊ธฐ