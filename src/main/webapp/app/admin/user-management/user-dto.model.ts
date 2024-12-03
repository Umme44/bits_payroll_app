export class UserDTOModel {

  constructor(public username: string = '',
              public continuousFailedAttempts: number = 0)
  {
  }

}
