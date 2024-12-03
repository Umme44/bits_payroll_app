import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IUserFeedback } from '../legacy-model/user-feedback.model';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IUserFeedback>;
type EntityArrayResponseType = HttpResponse<IUserFeedback[]>;

@Injectable({ providedIn: 'root' })
export class UserFeedbackService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/user-feedbacks');
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/common/user-feedbacks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  isAllowed(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrlCommon + '/is-allowed', { observe: 'response' });
  }

  create(userFeedback: IUserFeedback): Observable<EntityResponseType> {
    return this.http.post<IUserFeedback>(this.resourceUrl, userFeedback, { observe: 'response' });
  }

  createCommon(userFeedback: IUserFeedback): Observable<EntityResponseType> {
    return this.http.post<IUserFeedback>(this.resourceUrlCommon, userFeedback, { observe: 'response' });
  }

  update(userFeedback: IUserFeedback): Observable<EntityResponseType> {
    return this.http.put<IUserFeedback>(this.resourceUrl, userFeedback, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserFeedback>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserFeedback[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
