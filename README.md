# 🌐 Senior Telecom - App Multiplataforma

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-000000?style=for-the-badge&logo=apple&logoColor=white)
![Compose](https://img.shields.io/badge/Compose_Multiplatform-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)

Este repositório contém o aplicativo oficial da **Senior Telecom**, desenvolvido para proporcionar aos assinantes uma experiência ágil e intuitiva no acesso à central de serviços. Utilizando **Kotlin Multiplatform (KMP)**, compartilhamos a lógica de negócio e a interface entre Android e iOS.

---

## 📸 Screenshots (Em breve)
| Android | iOS |
| :---: | :---: |
| ![Android Screenshot](https://via.placeholder.com/200x400?text=Em+Breve) | ![iOS Screenshot](https://via.placeholder.com/200x400?text=Em+Breve) |

---

## ✨ Funcionalidades Atuais

- [x] **Integração com Central do Assinante:** Carregamento rápido via WebView nativa.
- [x] **Interface Unificada:** UI construída com Compose Multiplatform (Material 3).
- [x] **Suporte a Safe Areas:** Adaptado para celulares com notch (iPhone e Android modernos).
- [x] **Performance Nativa:** Utiliza componentes de visualização web nativos de cada sistema (Android WebView e WKWebView).

---

## 🛠 Detalhes Técnicos

O projeto está estruturado para maximizar o compartilhamento de código:

- **`composeApp/commonMain`**: Onde reside toda a mágica. Telas, temas e lógica de navegação.
- **`composeApp/androidMain`**: Configurações específicas do Android, manifesto e permissões de internet.
- **`composeApp/iosMain`**: Interoperabilidade com iOS e chamadas nativas do sistema Apple.

### Requisitos para Desenvolvimento
- **Android Studio** (Versão Ladybug ou superior preferencialmente).
- **Kotlin Multiplatform SDK**.
- **Xcode** (Apenas para rodar/compilar a versão iOS).

---

## 🚀 Como Rodar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/senior_telecom_multiplataforma.git
   ```
2. Abra no **Android Studio**.
3. Aguarde a sincronização do **Gradle**.
4. Para Android: Selecione a configuração `composeApp` e dê Play.
5. Para iOS: Use o seletor de dispositivos do Android Studio ou abra a pasta `iosApp` no Xcode.

---

## 📝 Licença
Este projeto é de propriedade da **Senior Telecom**. Todos os direitos reservados.

---
*Desenvolvido com ❤️ usando Kotlin Multiplatform.*
