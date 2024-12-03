import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAitConfig, NewAitConfig } from '../ait-config.model';

export type PartialUpdateAitConfig = Partial<IAitConfig> & Pick<IAitConfig, 'id'>;

type RestOf<T extends IAitConfig | NewAitConfig> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestAitConfig = RestOf<IAitConfig>;

export type NewRestAitConfig = RestOf<NewAitConfig>;

export type PartialUpdateRestAitConfig = RestOf<PartialUpdateAitConfig>;

export type EntityResponseType = HttpResponse<IAitConfig>;
export type EntityArrayResponseType = HttpResponse<IAitConfig[]>;

@Injectable({ providedIn: 'root' })
export class AitConfigService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ait/configs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aitConfig: IAitConfig): Observable<HttpResponse<any>> {
    const copy = this.convertDateFromClient(aitConfig);
    return this.http.post<IAitConfig>(this.resourceUrl, copy, { observe: 'response' });
  }

  update(aitConfig: IAitConfig): Observable<HttpResponse<any>> {
    const copy = this.convertDateFromClient(aitConfig);
    return this.http.put<IAitConfig>(this.resourceUrl, copy, { observe: 'response' });
  }

  partialUpdate(aitConfig: PartialUpdateAitConfig): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aitConfig);
    return this.http
      .patch<RestAitConfig>(`${this.resourceUrl}/${this.getAitConfigIdentifier(aitConfig)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAitConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAitConfig[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAitConfigIdentifier(aitConfig: Pick<IAitConfig, 'id'>): number {
    return aitConfig.id;
  }

  compareAitConfig(o1: Pick<IAitConfig, 'id'> | null, o2: Pick<IAitConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getAitConfigIdentifier(o1) === this.getAitConfigIdentifier(o2) : o1 === o2;
  }

  addAitConfigToCollectionIfMissing<Type extends Pick<IAitConfig, 'id'>>(
    aitConfigCollection: Type[],
    ...aitConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aitConfigs: Type[] = aitConfigsToCheck.filter(isPresent);
    if (aitConfigs.length > 0) {
      const aitConfigCollectionIdentifiers = aitConfigCollection.map(aitConfigItem => this.getAitConfigIdentifier(aitConfigItem)!);
      const aitConfigsToAdd = aitConfigs.filter(aitConfigItem => {
        const aitConfigIdentifier = this.getAitConfigIdentifier(aitConfigItem);
        if (aitConfigCollectionIdentifiers.includes(aitConfigIdentifier)) {
          return false;
        }
        aitConfigCollectionIdentifiers.push(aitConfigIdentifier);
        return true;
      });
      return [...aitConfigsToAdd, ...aitConfigCollection];
    }
    return aitConfigCollection;
  }

  protected convertDateFromClient<T extends IAitConfig | NewAitConfig | PartialUpdateAitConfig>(aitConfig: T): RestOf<T> {
    return {
      ...aitConfig,
      startDate: aitConfig.startDate?.format(DATE_FORMAT) ?? null,
      endDate: aitConfig.endDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAitConfig: RestAitConfig): IAitConfig {
    return {
      ...restAitConfig,
      startDate: restAitConfig.startDate ? dayjs(restAitConfig.startDate) : undefined,
      endDate: restAitConfig.endDate ? dayjs(restAitConfig.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAitConfig>): HttpResponse<IAitConfig> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAitConfig[]>): HttpResponse<IAitConfig[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
