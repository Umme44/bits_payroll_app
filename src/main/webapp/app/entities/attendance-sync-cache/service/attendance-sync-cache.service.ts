import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttendanceSyncCache, NewAttendanceSyncCache } from '../attendance-sync-cache.model';

export type PartialUpdateAttendanceSyncCache = Partial<IAttendanceSyncCache> & Pick<IAttendanceSyncCache, 'id'>;

type RestOf<T extends IAttendanceSyncCache | NewAttendanceSyncCache> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestAttendanceSyncCache = RestOf<IAttendanceSyncCache>;

export type NewRestAttendanceSyncCache = RestOf<NewAttendanceSyncCache>;

export type PartialUpdateRestAttendanceSyncCache = RestOf<PartialUpdateAttendanceSyncCache>;

export type EntityResponseType = HttpResponse<IAttendanceSyncCache>;
export type EntityArrayResponseType = HttpResponse<IAttendanceSyncCache[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceSyncCacheService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-sync-caches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendanceSyncCache: NewAttendanceSyncCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSyncCache);
    return this.http
      .post<RestAttendanceSyncCache>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(attendanceSyncCache: IAttendanceSyncCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSyncCache);
    return this.http
      .put<RestAttendanceSyncCache>(`${this.resourceUrl}/${this.getAttendanceSyncCacheIdentifier(attendanceSyncCache)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(attendanceSyncCache: PartialUpdateAttendanceSyncCache): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceSyncCache);
    return this.http
      .patch<RestAttendanceSyncCache>(`${this.resourceUrl}/${this.getAttendanceSyncCacheIdentifier(attendanceSyncCache)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAttendanceSyncCache>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAttendanceSyncCache[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttendanceSyncCacheIdentifier(attendanceSyncCache: Pick<IAttendanceSyncCache, 'id'>): number {
    return attendanceSyncCache.id;
  }

  compareAttendanceSyncCache(o1: Pick<IAttendanceSyncCache, 'id'> | null, o2: Pick<IAttendanceSyncCache, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttendanceSyncCacheIdentifier(o1) === this.getAttendanceSyncCacheIdentifier(o2) : o1 === o2;
  }

  addAttendanceSyncCacheToCollectionIfMissing<Type extends Pick<IAttendanceSyncCache, 'id'>>(
    attendanceSyncCacheCollection: Type[],
    ...attendanceSyncCachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attendanceSyncCaches: Type[] = attendanceSyncCachesToCheck.filter(isPresent);
    if (attendanceSyncCaches.length > 0) {
      const attendanceSyncCacheCollectionIdentifiers = attendanceSyncCacheCollection.map(
        attendanceSyncCacheItem => this.getAttendanceSyncCacheIdentifier(attendanceSyncCacheItem)!
      );
      const attendanceSyncCachesToAdd = attendanceSyncCaches.filter(attendanceSyncCacheItem => {
        const attendanceSyncCacheIdentifier = this.getAttendanceSyncCacheIdentifier(attendanceSyncCacheItem);
        if (attendanceSyncCacheCollectionIdentifiers.includes(attendanceSyncCacheIdentifier)) {
          return false;
        }
        attendanceSyncCacheCollectionIdentifiers.push(attendanceSyncCacheIdentifier);
        return true;
      });
      return [...attendanceSyncCachesToAdd, ...attendanceSyncCacheCollection];
    }
    return attendanceSyncCacheCollection;
  }

  protected convertDateFromClient<T extends IAttendanceSyncCache | NewAttendanceSyncCache | PartialUpdateAttendanceSyncCache>(
    attendanceSyncCache: T
  ): RestOf<T> {
    return {
      ...attendanceSyncCache,
      timestamp: attendanceSyncCache.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAttendanceSyncCache: RestAttendanceSyncCache): IAttendanceSyncCache {
    return {
      ...restAttendanceSyncCache,
      timestamp: restAttendanceSyncCache.timestamp ? dayjs(restAttendanceSyncCache.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAttendanceSyncCache>): HttpResponse<IAttendanceSyncCache> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAttendanceSyncCache[]>): HttpResponse<IAttendanceSyncCache[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
