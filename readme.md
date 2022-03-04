# Auction!

> *minecraft auction plugin*
>

## Todo

- 기본적인 Auction!
  - 💰재화 MONEY!
  - **🤝**아이템 구매 & 판매
  - 📈시세 검색
  - ❤️장바구니

## 구현

- 재화 & 아이템 저장
  - 재화를 저장하는 방법은 굉장히 많았습니다.
    - yaml 파일저장
    - nbt
  - 하지만 그딴거 없다 바로 DB때려박기
  - JDBC 사용하기 build.gradle.kts

    ```kotlin
    repository {
        mavenCentral()
    }
    
    dependencies {
        implementation("mysql:mysql-connector-java:8.0.28")
    }
    ```

  ~~하하핳~~

  - 이후 object AuctionDatabase에서 DB와 통신하면 된다

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

  ~~사실 DB 써보고 싶었던건 안비밀~~

- 아이템 판매& 구매
  - [monun](http://github.com/monun)의 invfx와 kommand를 사용했다.

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

  - 이렇게 아이템들을 가져올 수 있고 select 문에 where 을 사용하여 필터도 사용할 수 있다.
- 장바구니
  - DB 테이블에 shopping_bag 테이블을 추가, 이 테이블에서 무슨 아이템이 누구의 장바구니에 들어있는지 확인하여 표시한다
  - TODO 장바구니안의 아이템 삭제 & item 테이블에서 삭제된 값은 같이 삭제  (user 포함)
    - foreign key 사용하기