# RP-as-Library: 設計提案

日付: 2025-11-02

目的
- `rpserver` に実装されている WebAuthn(FIDO2) のユースケースをライブラリ（コア）として切り出し、アプリケーションはそのライブラリを依存して利用する構成にする。

要約
- いま: `rpserver` と `demo` が重複して FIDO2 ロジックを持ち、責務が散在している。
- あるべき姿: `fido2-core`（または新しい `fido2-webauthn`）を純粋なライブラリ化コアにし、`rpserver` は薄い HTTP アダプタ（Controller + 簡単な構成）として振る舞う。`demo` は参照実装にするか CI から切り離す。

モジュール境界（提案）
- common: 既存の `common`（そのまま）
- fido2-core (or fido2-webauthn): ビジネスロジック（チャレンジ生成、検証、セッション操作、MDS 検証、鍵管理）と public API を公開
- rpserver: HTTP 層（Spring Controller・Adapter）、設定（application.yml）、セキュリティ・認可のラッパー
- demo: 参考実装（fido2-core を使うサンプルアプリ）。CI での必須度はオプション。

Public API の骨子（ライブラリ側）
- package: `com.linecorp.line.auth.fido.fido2.core`（例）
- サービスインターフェース例:
  - `interface ChallengeService { RegOptionResponse getRegChallenge(RegOptionRequest req); AuthOptionResponse getAuthChallenge(AuthOptionRequest req); }`
  - `interface ResponseService { ServerResponse handleRegistration(RegisterCredential reg, String sessionId); ServerResponse handleAuthentication(VerifyCredential verify, String sessionId); }`
  - `interface SessionRepository { Session create(Session s); Session find(String id); void delete(String id); }`

移行手順（段階的）
1. 設計ドキュメント合意（このドキュメント）
2. `rpserver` 内でコアロジックを担うクラスのリストアップ（候補抽出）
3. まずインターフェースを `fido2-core` に追加し、`rpserver` はインターフェース依存に変える（実装はまだ rpserver に残す）
4. 小さな単位（例: ChallengeService）を実装ごと `fido2-core` に移す。移行ごとにユニット/統合テストを回す
5. `rpserver` は `fido2-core` に依存を追加（Gradle の project(':fido2-core') など）
6. `demo` を `fido2-core` を使う参照実装に更新、もしくは CI から除外

Gradle/ビルドの変更例
- `settings.gradle`: 新しいモジュール `fido2-webauthn` を追加（必要なら）
- `rpserver/build.gradle`:
  - 依存に `implementation project(':fido2-core')`
  - テストは core と rpserver を別に実行できるようにする

テストの方針
- core はユニットテスト中心（ロジックとインターフェースの契約）
- rpserver はコントローラの統合テスト（Mock core 実装を注入して Controller レベルだけ確認）
- demo はエンドツーエンドまたは参照用の統合テストに限定

互換性とリスク
- 既存パッケージ名や public model を変更すると downstream に影響が大きい。可能な限りパッケージ・クラス名は保持してインターフェースを抽出する方式で互換性を保つ。
- Bean 名の衝突（例: `RestExceptionHandler`）は各モジュールの component-scan 範囲を限定する、または明示的な `@ComponentScan(excludeFilters=...)`/`@Import` を使って解決する。

ローリングアウト戦略
1. 小さな PR（1つのサービス/インターフェース単位）で段階的に移行
2. 各 PR で core のユニットテストと rpserver の Controller テストを必須にする
3. 移行が完了したら `demo` を参照実装として README に記載し、CI の default pipeline からは外す（必要なら nightly で実行）

次の具体アクション（私が提案する順序）
1. `rpserver` のサービス候補（最初は `ChallengeService`, `ResponseService`, `SessionRepository`）をソースから一覧化 -> これを私が出します
2. 最初のインターフェース抽出（`ChallengeService`）を `fido2-core` に追加し、rpserver をインターフェース依存に切り替える（小さな PR）
3. core の実装を一部移動してテストを回す

備考
- このドキュメントは短期的な実行計画です。詳細な API ドキュメント（型定義、例外、エラーコード）は最初のインターフェース抽出の際に補っていきます。

---
作成者: 自動生成（ペアプログラミング支援）
