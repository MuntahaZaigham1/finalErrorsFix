import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { By } from '@angular/platform-browser';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { of } from 'rxjs';

import { TestingModule, EntryComponents, checkValues } from 'src/testing/utils';
import { IT2,T2Service, T2NewComponent } from '../';
import { DateUtils } from 'src/app/common/shared';
import { NewComponent, FieldsComp } from 'src/app/common/general-components';

describe('T2NewComponent', () => {
  let component: T2NewComponent;
  let fixture: ComponentFixture<T2NewComponent>;
  
  let el: HTMLElement;
  
  let d = new Date();
  let t = DateUtils.formatDateStringToAMPM(d);
  
  let relationData: any = {
  }
  let data:IT2 = {
	  id: 1,
	  pic: 'pic1',
	  ... relationData
  };
  
  let formData = {...data};

  describe('Unit tests', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        declarations: [
          T2NewComponent,
          NewComponent,
          FieldsComp
        ],
        imports: [TestingModule],
        providers: [
          T2Service,
          { provide: MAT_DIALOG_DATA, useValue: {} }
        ],
        schemas: [NO_ERRORS_SCHEMA]
      }).compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(T2NewComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should run #ngOnInit()', async(() => {
      component.ngOnInit();

      expect(component.title.length).toBeGreaterThan(0);
      expect(component.associations).toBeDefined();
      expect(component.parentAssociations).toBeDefined();
      expect(component.itemForm).toBeDefined();
    }));

    it('should run #onSubmit()', async () => {
      component.itemForm.patchValue(data);
      component.itemForm.enable();
      fixture.detectChanges();
      spyOn(component, 'onSubmit').and.returnValue();
      el = fixture.debugElement.query(By.css('button[name=save]')).nativeElement;
      el.click();
      expect(component.onSubmit).toHaveBeenCalled();
    });

    it('should call the cancel', async () => {
      spyOn(component, 'onCancel').and.callThrough();
      el = fixture.debugElement.query(By.css('a[name=cancel]')).nativeElement;
      el.click();
      expect(component.onCancel).toHaveBeenCalled();
    });
  })

  describe('Integration tests', () => {

    // had to create a different suite because couldn't override MAT_DIALOG_DATA provider
    describe('', () => {
      it('should set the passed data to form', async () => {

        TestBed.configureTestingModule({
          declarations: [
            T2NewComponent,
	          NewComponent,
	          FieldsComp
          ].concat(EntryComponents),
          imports: [TestingModule],
          providers: [
            T2Service,
            { provide: MAT_DIALOG_DATA, useValue: relationData }
          ]
        });
        TestBed.overrideProvider(MAT_DIALOG_DATA, {useValue : relationData})
        fixture = TestBed.createComponent(T2NewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
  
        component.checkPassedData();
        fixture.detectChanges();
        expect(checkValues(component.itemForm.getRawValue(), relationData)).toBe(true);
      });
    })

    describe('', () => {
      beforeEach(async(() => {
        TestBed.configureTestingModule({
          declarations: [
            T2NewComponent,
	          NewComponent,
	          FieldsComp
          ].concat(EntryComponents),
          imports: [TestingModule],
          providers: [
            T2Service,
            { provide: MAT_DIALOG_DATA, useValue: {} }
          ]
        });
      }));
  
      beforeEach(() => {
        fixture = TestBed.createComponent(T2NewComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
      });
  
      it('should create', () => {
        expect(component).toBeTruthy();
      });
  
      it('should run #ngOnInit()', async(() => {
        component.ngOnInit();
  
        expect(component.title.length).toBeGreaterThan(0);
        expect(component.associations).toBeDefined();
        expect(component.parentAssociations).toBeDefined();
        expect(component.itemForm).toBeDefined();
        expect(component.data).toEqual({});
      }));
  
      it('should create the record and close the dialog with created object response', async () => {
        component.itemForm.patchValue(data);
        component.itemForm.enable();
        fixture.detectChanges();
        spyOn(component.dialogRef, 'close').and.returnValue();
        spyOn(component.dataService, 'create').and.returnValue(of(data));
  
        el = fixture.debugElement.query(By.css('button[name=save]')).nativeElement;
        el.click();
        expect(component.dialogRef.close).toHaveBeenCalledWith(data);
      });
  
      it('should close the dialog with null data when cancel button is pressed', async () => {
        spyOn(component.dialogRef, 'close').and.returnValue();
        el = fixture.debugElement.query(By.css('a[name=cancel]')).nativeElement;
        el.click();
        expect(component.dialogRef.close).toHaveBeenCalledWith(null);
      });

    })

  })
  
});