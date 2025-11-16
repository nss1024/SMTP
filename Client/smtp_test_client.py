import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

# SMTP server config
HOST = "127.0.0.1"
PORT = 8029  # or your server port

# Email info
sender = "alice@example.com"
recipients = ["bob@example.com","simon@myserver.com"]
subject = "Test Email"
body = "Hello Bob, this is a test email from Python client."

# Create MIME message
msg = MIMEMultipart()
msg["From"] = sender
msg["To"] = ", ".join(recipients)
msg["Subject"] = subject
msg.attach(MIMEText(body, "plain"))

# Connect and send email
with smtplib.SMTP(HOST, PORT) as server:
    server.set_debuglevel(1)  # prints full conversation
    server.ehlo()
    server.sendmail(sender, recipients, msg.as_string())
    server.quit()
