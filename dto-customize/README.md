# Kotlin用いたOpenApiの使用方法について

## モデルを作成するためのYAMLファイルの書き方

```yaml
openapi: 3.1.0
info:
  title: Sample API
  version: 1.0.
components:
  schemas:
    SampleModel:
      type: object
      properties:
        id:
          type: integer
          format: int64
          description: "The unique identifier for a sample model"
        name:
          type: string
          description: "The name of the sample model"
```

## 生成するモデルの表示形式を変える方法

- **Mustacheテンプレートを編集する**
- モデルを編集する場合は以下のテンプレートを編集する:
    1. **dataClass.mustache**
    2. **model.mustache**
- <a href="https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator/src/main/resources/kotlin-spring" target="_blank" rel="noopener noreferrer">元となるテンプレートの公式リポジトリ</a>
    1. <a href="https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/kotlin-spring/dataClass.mustache" target="_blank" rel="noopener noreferrer">dataClass.mustache</a>
    2. <a href="https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/kotlin-spring/model.mustache" target="_blank" rel="noopener noreferrer">model.mustache</a>
- `{{???}}` の形でテンプレートをカスタマイズすることができます。


## YAMLに独自のプロパティを定義し、Mustacheテンプレートで読み込めるようにする

- 独自のプロパティを作る

```yaml
openapi: 3.1.0
info:
  title: Sample API
  version: 1.0.0
components:
  schemas:
    PackageSelectModel:
      custom-package: "com.app.model.xxx" #独自のプロパティを作る
      type: object
      properties:
        id:
          type: integer
          format: int64
          description: "The unique identifier for a sample model"
        name:
          type: string
          description: "The name of the sample model"
```

- Mustacheテンプレートで独自のプロパティを使う場合は、 **vendorExtensions.{独自のプロパティ}** をつけて設定すると読み込まれる

```mustache
package {{vendorExtensions.custom-package}}

data class {{classname}} (
    {{#vars}}
    // {{description}}
    val {{baseName}}: {{datatypeWithEnum}},
    {{/vars}}
)
```