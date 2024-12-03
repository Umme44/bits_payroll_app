import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEventLog, NewEventLog } from '../event-log.model';

export type PartialUpdateEventLog = Partial<IEventLog> & Pick<IEventLog, 'id'>;

type RestOf<T extends IEventLog | NewEventLog> = Omit<T, 'performedAt'> & {
  performedAt?: string | null;
};

export type RestEventLog = RestOf<IEventLog>;

export type NewRestEventLog = RestOf<NewEventLog>;

export type PartialUpdateRestEventLog = RestOf<PartialUpdateEventLog>;

export type EntityResponseType = HttpResponse<IEventLog>;
export type EntityArrayResponseType = HttpResponse<IEventLog[]>;

@Injectable({ providedIn: 'root' })
export class EventLogService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/event-logs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eventLog: NewEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .post<RestEventLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eventLog: IEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .put<RestEventLog>(`${this.resourceUrl}/${this.getEventLogIdentifier(eventLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eventLog: PartialUpdateEventLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventLog);
    return this.http
      .patch<RestEventLog>(`${this.resourceUrl}/${this.getEventLogIdentifier(eventLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEventLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEventLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEventLogIdentifier(eventLog: Pick<IEventLog, 'id'>): number {
    return eventLog.id;
  }

  compareEventLog(o1: Pick<IEventLog, 'id'> | null, o2: Pick<IEventLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getEventLogIdentifier(o1) === this.getEventLogIdentifier(o2) : o1 === o2;
  }

  addEventLogToCollectionIfMissing<Type extends Pick<IEventLog, 'id'>>(
    eventLogCollection: Type[],
    ...eventLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eventLogs: Type[] = eventLogsToCheck.filter(isPresent);
    if (eventLogs.length > 0) {
      const eventLogCollectionIdentifiers = eventLogCollection.map(eventLogItem => this.getEventLogIdentifier(eventLogItem)!);
      const eventLogsToAdd = eventLogs.filter(eventLogItem => {
        const eventLogIdentifier = this.getEventLogIdentifier(eventLogItem);
        if (eventLogCollectionIdentifiers.includes(eventLogIdentifier)) {
          return false;
        }
        eventLogCollectionIdentifiers.push(eventLogIdentifier);
        return true;
      });
      return [...eventLogsToAdd, ...eventLogCollection];
    }
    return eventLogCollection;
  }

  protected convertDateFromClient<T extends IEventLog | NewEventLog | PartialUpdateEventLog>(eventLog: T): RestOf<T> {
    return {
      ...eventLog,
      performedAt: eventLog.performedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEventLog: RestEventLog): IEventLog {
    return {
      ...restEventLog,
      performedAt: restEventLog.performedAt ? dayjs(restEventLog.performedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEventLog>): HttpResponse<IEventLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEventLog[]>): HttpResponse<IEventLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
