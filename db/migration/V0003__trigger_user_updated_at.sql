CREATE TRIGGER update_updated_at
    BEFORE UPDATE
    ON
        user_account
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();
