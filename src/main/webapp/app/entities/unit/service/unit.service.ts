import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUnit, NewUnit } from '../unit.model';

export type PartialUpdateUnit = Partial<IUnit> & Pick<IUnit, 'id'>;

export type EntityResponseType = HttpResponse<IUnit>;
export type EntityArrayResponseType = HttpResponse<IUnit[]>;

@Injectable({ providedIn: 'root' })
export class UnitService {
  // protected resourceUrl = this.applicationConfigService.getEndpointFor('api/units');
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(unit: NewUnit): Observable<EntityResponseType> {
    return this.http.post<IUnit>(this.resourceUrl, unit, { observe: 'response' });
  }

  update(unit: IUnit): Observable<EntityResponseType> {
    return this.http.put<IUnit>(this.resourceUrl, unit, { observe: 'response' });
  }

  partialUpdate(unit: PartialUpdateUnit): Observable<EntityResponseType> {
    return this.http.patch<IUnit>(`${this.resourceUrl}/${this.getUnitIdentifier(unit)}`, unit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryPage(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUnit[]>(`${this.resourceUrl}/page`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUnitIdentifier(unit: Pick<IUnit, 'id'>): number {
    return unit.id;
  }

  compareUnit(o1: Pick<IUnit, 'id'> | null, o2: Pick<IUnit, 'id'> | null): boolean {
    return o1 && o2 ? this.getUnitIdentifier(o1) === this.getUnitIdentifier(o2) : o1 === o2;
  }

  addUnitToCollectionIfMissing<Type extends Pick<IUnit, 'id'>>(
    unitCollection: Type[],
    ...unitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const units: Type[] = unitsToCheck.filter(isPresent);
    if (units.length > 0) {
      const unitCollectionIdentifiers = unitCollection.map(unitItem => this.getUnitIdentifier(unitItem)!);
      const unitsToAdd = units.filter(unitItem => {
        const unitIdentifier = this.getUnitIdentifier(unitItem);
        if (unitCollectionIdentifiers.includes(unitIdentifier)) {
          return false;
        }
        unitCollectionIdentifiers.push(unitIdentifier);
        return true;
      });
      return [...unitsToAdd, ...unitCollection];
    }
    return unitCollection;
  }
}
