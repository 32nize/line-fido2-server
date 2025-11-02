# FIDO2 サーバー実装の構成図

## モジュール関係図
```mermaid
graph TB
    Client[クライアント/ブラウザ]
    subgraph "FIDO2 サーバー実装"
        Demo[Demo Server<br/>fido2-demo/demo]
        Base[Demo Base<br/>fido2-demo/base]
        Core[FIDO2 Core<br/>fido2-core]
        Common[Common<br/>common]
    end
    RPServer[RP Server<br/>rpserver]
    MDS["FIDO Metadata Service<br/>(External)"]

    Client -->|1. FIDO2 認証リクエスト| Demo
    Client -->|1. FIDO2 認証リクエスト| RPServer
    
    Demo -->|2. コア機能利用| Core
    RPServer -->|2. コア機能利用| Core
    
    Core -->|3. 共通機能利用| Common
    Demo -->|3. 共通機能利用| Common
    RPServer -->|3. 共通機能利用| Common
    
    Demo -->|4. メタデータ検証| MDS
    RPServer -->|4. メタデータ検証| MDS

    Base -->|依存| Core
    Demo -->|依存| Base

    classDef server fill:#f9f,stroke:#333,stroke-width:2px;
    classDef module fill:#bbf,stroke:#333,stroke-width:1px;
    classDef external fill:#ddd,stroke:#333,stroke-width:1px;
    
    class Demo,RPServer server;
    class Core,Common,Base module;
    class MDS external;
```
mコードを分析した結果、両サーバーのエンドポイントは以下のようになっています：

rpserver（RPサーバー）のエンドポイント：
認証関連：
```
POST /attestation/options   # 登録チャレンジの取得
POST /attestation/result   # 登録レスポンスの送信
POST /assertion/options    # 認証チャレンジの取得
POST /assertion/result     # 認証レスポンスの送信
```

認証情報管理：
```
GET    /credentials         # ユーザー名による認証情報の取得
GET    /credentials/{id}    # 認証情報IDによる認証情報の取得
DELETE /credentials         # ユーザー名による認証情報の削除
DELETE /credentials/{id}    # 認証情報IDによる認証情報の削除
```
その他：
```
GET /health        # ヘルスチェック
GET /index.html    # メインページ
```
demo（FIDO2サーバー）のエンドポイント：
FIDO2関連：
その他：
重要な観察：
アーキテクチャ上の問題:

現在、rpserverは実際にはFIDO2サーバー（demo）に依存していますが、直接依存ではなく、HTTP経由で通信
rpserverのAdapterControllerは、FIDO2サーバーのエンドポイントを呼び出す中継役として機能
推奨される改善点:

RPサーバー（rpserver）をクライアントの唯一のエントリーポイントとして使用
FIDO2サーバー（demo）をRPサーバーからのみアクセス可能な内部サービスとして配置
セッション管理やユーザー認証をRPサーバーに一元化
実際の依存関係:
```mermaid
sequenceDiagram
    participant Client as クライアント
    participant RP as RPサーバー<br/>(rpserver)
    participant FIDO2 as FIDO2サーバー<br/>(demo)
    
    Client->>RP: 登録/認証リクエスト
    RP->>FIDO2: チャレンジ生成要求
    FIDO2-->>RP: チャレンジ
    RP-->>Client: チャレンジ
    Client->>RP: レスポンス
    RP->>FIDO2: 検証要求
    FIDO2-->>RP: 検証結果
    RP-->>Client: 結果
```

    RP-->>Client: チャレンジ
この構造から、現在のプロジェクトはRPサーバーとFIDO2サーバーが分離されているものの、その間の依存関係が必ずしも最適化されていないことがわかります。