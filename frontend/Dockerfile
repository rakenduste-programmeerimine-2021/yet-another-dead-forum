FROM node:14.17.5-alpine

ENV HOME=/home/node

RUN mkdir -p $HOME/app
WORKDIR $HOME/app

COPY . $HOME/app

RUN npm install && \
    npm cache clean --force

ENTRYPOINT ["npm"]
CMD ["run build"]

EXPOSE 3000