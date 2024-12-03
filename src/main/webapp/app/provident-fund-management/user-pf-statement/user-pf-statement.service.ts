import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUserPfStatement } from '../../shared/model/user-pf/user-pf-statement.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../config/input.constants';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IUserPfStatement>;

@Injectable({ providedIn: 'root' })
export class UserPfStatementService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/user-pf-statement');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  generateUserPfStatement(date: any): Observable<EntityResponseType> {
    const momentDate = dayjs(date).format(DATE_FORMAT);
    // @ts-ignore
    return this.http.get<IUserPfStatement>(this.applicationConfigService.getEndpointFor('/api/common/my-pf-statement/' + momentDate), {
      observe: 'response',
    });
  }

  checkValidityOfUserPfStatement(): Observable<boolean> {
    return this.http.get<boolean>(`${this.resourceUrl}/validity`);
  }
}
