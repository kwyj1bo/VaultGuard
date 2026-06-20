VaultGuard is a robust Just-In-Time (JIT) privileged access management system designed to secure administrative operations. By combining ephemeral access patterns, dynamic secret injection, and real-time session monitoring, VaultGuard drastically reduces the attack surface for sensitive infrastructure.
🚀 Key Features
JIT Privilege Management: Enforces time-bound access, automatically terminating sessions after 120 minutes to adhere to the Principle of Least Privilege.
Dynamic Credential Injection: Integrates directly with HashiCorp Vault to retrieve administrative credentials on-the-fly, ensuring zero hard-coded secrets remain in the application environment.
Real-Time Monitoring: Utilizes WebSockets for a low-latency audit trail of all administrative actions.
Automated Kill-Switch: An intelligent monitoring engine capable of identifying and immediately halting unauthorized or anomalous operations.
🛠️ Tech Stack
Core: Java, Spring Boot, Spring Security
Caching & Session Management: Redis
Secret Management: HashiCorp Vault
Communication: WebSockets
Database: PostgreSQL
🏗️ System Architecture
VaultGuard orchestrates secure access by brokering identity between the user, the Vault secret engine, and the target infrastructure.
⚙️ How It Works
Authentication & JIT Request: When an administrator requests access, Spring Security validates the identity and initiates a time-limited session.
Secret Injection: Upon session approval, the application fetches short-lived credentials from HashiCorp Vault, injecting them into the session environment without ever persisting them to disk.
Real-Time Auditing: Every command executed during the session is streamed via WebSockets to the auditing engine.
Session Termination: Once the 120-minute threshold is reached (or a manual kill-switch is triggered), the Redis-backed session is invalidated, and the credentials are revoked in Vault.
🛡️ Security Impact
100% Elimination of Hard-coded Secrets: By utilizing dynamic injection, sensitive administrative credentials are never stored in the application layer.
Minimized Blast Radius: The strict 120-minute session expiry ensures that even if a session is compromised, the window of opportunity for an attacker is tightly controlled.
Proactive Threat Mitigation: The automated kill-switch provides an active defense mechanism rather than passive logging.
📦 Getting Started
Prerequisites
Java 17+
PostgreSQL
Redis
HashiCorp Vault instance
Installation
Clone the repository: git clone https://github.com/kwyj1bo/VaultGuard.git
Configure your environment variables for Vault and Redis.
Run the application: ./mvnw spring-boot:run
