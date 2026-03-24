import json
import uuid
import datetime
import time
from faker import Faker
from confluent_kafka import SerializingProducer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroSerializer

# Configurações
BOOTSTRAP_SERVERS = 'localhost:9092'
SCHEMA_REGISTRY_URL = 'http://localhost:8081'
TOPIC = 'task-management.task-created.v1'
SCHEMA_FILE = 'src/main/resources/avro/task_created.avsc'

fake = Faker(['pt_BR'])

def load_schema(file_path):
    with open(file_path, 'r') as f:
        return f.read()

def delivery_report(err, msg):
    if err is not None:
        print(f'❌ Erro ao entregar mensagem: {err}')
    else:
        print(f'✅ Evento entregue no tópico {msg.topic()} [partição {msg.partition()}]')

# 1. Configurar Schema Registry Client
schema_registry_conf = {'url': SCHEMA_REGISTRY_URL}
schema_registry_client = SchemaRegistryClient(schema_registry_conf)

# 2. Configurar Avro Serializer
avro_serializer = AvroSerializer(schema_registry_client, 
                                 load_schema(SCHEMA_FILE))

# 3. Configurar Producer
producer_conf = {
    'bootstrap.servers': BOOTSTRAP_SERVERS,
    'value.serializer': avro_serializer,
    'acks': 'all'
}
producer = SerializingProducer(producer_conf)

def generate_and_send():
    task_id = str(uuid.uuid4())
    correlation_id = f"py-stimulus-{int(time.time())}"
    
    # Gerando dados realistas com Faker
    payload = {
        "eventId": str(uuid.uuid4()),
        "transactionId": str(uuid.uuid4()),
        "correlationId": correlation_id,
        "timestamp": datetime.datetime.now(datetime.timezone.utc).isoformat(),
        "payload": {
            "taskId": task_id,
            "title": fake.catch_phrase(),
            "description": f"{fake.job()}: {fake.sentence()}",
            "status": "PENDING"
        }
    }

    print(f"🚀 Enviando Task: {payload['payload']['title']}")
    print(f"🔗 CorrelationId: {correlation_id}")

    producer.produce(topic=TOPIC, value=payload, on_delivery=delivery_report)
    producer.flush()

if __name__ == "__main__":
    try:
        # Você pode rodar um loop aqui se quiser gerar massa
        generate_and_send()
    except Exception as e:
        print(f"💥 Falha crítica no script: {e}")
