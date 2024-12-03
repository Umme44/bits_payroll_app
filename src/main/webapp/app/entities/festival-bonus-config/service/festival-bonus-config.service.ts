import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFestivalBonusConfig, NewFestivalBonusConfig } from '../festival-bonus-config.model';

export type PartialUpdateFestivalBonusConfig = Partial<IFestivalBonusConfig> & Pick<IFestivalBonusConfig, 'id'>;

export type EntityResponseType = HttpResponse<IFestivalBonusConfig>;
export type EntityArrayResponseType = HttpResponse<IFestivalBonusConfig[]>;

@Injectable({ providedIn: 'root' })
export class FestivalBonusConfigService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/festival-bonus-configs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(festivalBonusConfig: NewFestivalBonusConfig): Observable<EntityResponseType> {
    return this.http.post<IFestivalBonusConfig>(this.resourceUrl, festivalBonusConfig, { observe: 'response' });
  }

  update(festivalBonusConfig: IFestivalBonusConfig): Observable<EntityResponseType> {
    return this.http.put<IFestivalBonusConfig>(
      `${this.resourceUrl}`,
      festivalBonusConfig,
      { observe: 'response' }
    );
  }

  partialUpdate(festivalBonusConfig: PartialUpdateFestivalBonusConfig): Observable<EntityResponseType> {
    return this.http.patch<IFestivalBonusConfig>(
      `${this.resourceUrl}/${this.getFestivalBonusConfigIdentifier(festivalBonusConfig)}`,
      festivalBonusConfig,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFestivalBonusConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFestivalBonusConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFestivalBonusConfigIdentifier(festivalBonusConfig: Pick<IFestivalBonusConfig, 'id'>): number {
    return festivalBonusConfig.id;
  }

  compareFestivalBonusConfig(o1: Pick<IFestivalBonusConfig, 'id'> | null, o2: Pick<IFestivalBonusConfig, 'id'> | null): boolean {
    return o1 && o2 ? this.getFestivalBonusConfigIdentifier(o1) === this.getFestivalBonusConfigIdentifier(o2) : o1 === o2;
  }

  addFestivalBonusConfigToCollectionIfMissing<Type extends Pick<IFestivalBonusConfig, 'id'>>(
    festivalBonusConfigCollection: Type[],
    ...festivalBonusConfigsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const festivalBonusConfigs: Type[] = festivalBonusConfigsToCheck.filter(isPresent);
    if (festivalBonusConfigs.length > 0) {
      const festivalBonusConfigCollectionIdentifiers = festivalBonusConfigCollection.map(
        festivalBonusConfigItem => this.getFestivalBonusConfigIdentifier(festivalBonusConfigItem)!
      );
      const festivalBonusConfigsToAdd = festivalBonusConfigs.filter(festivalBonusConfigItem => {
        const festivalBonusConfigIdentifier = this.getFestivalBonusConfigIdentifier(festivalBonusConfigItem);
        if (festivalBonusConfigCollectionIdentifiers.includes(festivalBonusConfigIdentifier)) {
          return false;
        }
        festivalBonusConfigCollectionIdentifiers.push(festivalBonusConfigIdentifier);
        return true;
      });
      return [...festivalBonusConfigsToAdd, ...festivalBonusConfigCollection];
    }
    return festivalBonusConfigCollection;
  }
}
