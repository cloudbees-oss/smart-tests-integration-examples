FROM ubuntu:latest

COPY . /opt/
WORKDIR /opt/
RUN apt -y update && \
    apt -y install python3 python3-pip git zstd default-jdk cpm build-essential && \
    python3 -m pip install --upgrade pip && \
    pip install wheel setuptools_scm && \
    git clone https://github.com/launchableinc/cli.git && \
    cd cli && \
    git checkout fix-remove_teardown && \
    python3 setup.py install && \
    cd .. && \
    cpan Carton && \
    cpm install && \
    carton install && \
    cpan namespace::autoclean && \
    chmod +x /opt/run.sh
