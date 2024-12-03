import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { FlexScheduleApplicationService } from '../../../shared/legacy/legacy-service/flex-schedule-application.service';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<IFlexScheduleApplication>;
type EntityArrayResponseType = HttpResponse<IFlexScheduleApplication[]>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationUserService extends FlexScheduleApplicationService {
  public resourceUrl = SERVER_API_URL + 'api/common/flex-schedule-applications';

  create(flexScheduleApplication: IFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .post<IFlexScheduleApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(flexScheduleApplication: IFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .put<IFlexScheduleApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFlexScheduleApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFlexScheduleApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
